package ru.practicum.explore.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.InvalidParameterException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.mapper.RequestMapper;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.request.ParticipationRequest;
import ru.practicum.explore.model.request.ParticipationRequestDto;
import ru.practicum.explore.model.request.RequestStatus;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.RequestRepository;
import ru.practicum.explore.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestUserServiceImpl implements RequestUserService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        checkUserExist(userId);
        checkEventExist(eventId);

        Event event = eventRepository.getReferenceById(eventId);
        checkUserCanMakeRequest(event, userId);

        ParticipationRequest request = new ParticipationRequest();
        request.setRequesterId(userId);
        request.setCreated(LocalDateTime.now());
        request.setEventId(event.getId());
        request.setStatus(RequestStatus.PENDING);

        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        log.info("Request from user with id {} was created to event with id {}", userId, eventId);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }


    @Override
    public List<ParticipationRequestDto> findAllRequests(Long userId) {
        checkUserExist(userId);
        return RequestMapper.toParticipationRequestDtos(requestRepository.findAllByRequesterId(userId));
    }


    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        checkUserExist(userId);
        checkRequestExist(requestId);

        ParticipationRequest request = requestRepository.getReferenceById(requestId);

        if (request.getStatus().equals(RequestStatus.PENDING)) {
            request.setStatus(RequestStatus.CANCELED);
        } else if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            request.setStatus(RequestStatus.CANCELED);
            Event event = eventRepository.getReferenceById(request.getEventId());
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }
        log.info("Request with id {} was cancelled", requestId);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    private void checkEventExist(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.error("Event with id {} wasn't found", eventId);
            throw new ObjectNotFoundException(String.format("Event with id %d wasn't found", eventId));
        }
    }

    private void checkUserExist(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.error("User with id {} wasn't found", userId);
            throw new ObjectNotFoundException(String.format("User with id %d wasn't found", userId));
        }
    }

    private void checkRequestExist(Long requestId) {
        if (!requestRepository.existsById(requestId)) {
            log.error("Request with id {} wasn't found", requestId);
            throw new ObjectNotFoundException(String.format("Request with id %d wasn't found", requestId));
        }
    }

    private void checkUserCanMakeRequest(Event event, Long userId) {
        if (event.getInitiator().getId().equals(userId)) {
            log.error("User with id {} is the initiator of the event with id {}", userId, event.getId());
            throw new InvalidParameterException("User can't request his own event");
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() != null &&
                event.getConfirmedRequests().intValue() == event.getParticipantLimit()) {
            log.error("Participants limit to event with id {} has been reached", event.getId());
            throw new InvalidParameterException("The limit of the number of participants has been reached");
        }
        if (event.getState() != EventState.PUBLISHED) {
            log.error("Event with id {} isn't published", event.getId());
            throw new InvalidParameterException("Event hasn't been published");
        }
        if (!requestRepository.findAllByRequesterIdAndEventId(userId, event.getId()).isEmpty()) {
            log.error("User with id {} already has request to event with id {}", userId, event.getId());
            throw new InvalidParameterException("Request already exist");
        }
    }
}