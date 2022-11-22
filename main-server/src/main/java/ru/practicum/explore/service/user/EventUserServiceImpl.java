package ru.practicum.explore.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.InvalidParameterException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.mapper.RequestMapper;
import ru.practicum.explore.model.event.*;
import ru.practicum.explore.model.request.ParticipationRequest;
import ru.practicum.explore.model.request.ParticipationRequestDto;
import ru.practicum.explore.model.request.RequestStatus;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.RequestRepository;
import ru.practicum.explore.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventUserServiceImpl implements EventUserService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    public EventFullDto createEvent(Long userId, EventFullDto eventFullDto) {
        checkUserExist(userId);
        checkCategoryExist(eventFullDto.getCategory().getId());
        checkEventTimeIsCorrect(eventFullDto.getEventDate());
        log.info("New event was created by initiator id {}", userId);
        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.toEvent(eventFullDto)));
    }

    @Override
    public EventFullDto updateEvent(Long userId, UpdateEventRequest updateEventRequest) {
        checkUserExist(userId);
        checkEventExist(updateEventRequest.getEventId());
        if (updateEventRequest.getCategoryId() != null) {
            checkCategoryExist(updateEventRequest.getCategoryId());
        }
        if (updateEventRequest.getEventDate() != null) {
            checkEventTimeIsCorrect(updateEventRequest.getEventDate());
        }

        Event event = eventRepository.getReferenceById(updateEventRequest.getEventId());
        if (event.getState() == EventState.PUBLISHED) {
            throw new InvalidParameterException("Event is already published.");
        }
        if (event.getState() == EventState.CANCELED) {
            event.setState(EventState.PENDING);
        }

        log.info("Event with id {} was updated", updateEventRequest.getEventId());
        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.toUpdateEvent(
                event, EventMapper.toEventFromUpdateEventRequest(updateEventRequest))));
    }

    @Override
    public ParticipationRequestDto confirmRequestByInitiator(Long userId, Long eventId, Long requestId) {
        checkUserExist(userId);
        checkEventExist(eventId);
        checkRequestExist(requestId);

        ParticipationRequest request = requestRepository.getReferenceById(requestId);
        Event event = eventRepository.getReferenceById(eventId);

        if (requestRepository.findAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED).size() ==
                event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            log.info("Participants limit to event with id {} has been reached", eventId);
            throw new InvalidParameterException("The limit of the number of participants has been reached");
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1L);
            eventRepository.save(event);
        }

        if (event.getParticipantLimit() != 0 && event.getRequestModeration() &&
                requestRepository.findAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED).size() ==
                        event.getParticipantLimit()) {
            List<ParticipationRequest> pendingRequests = requestRepository.findAllByEventIdAndStatus(
                    eventId, RequestStatus.PENDING);
            pendingRequests.forEach(r -> r.setStatus(RequestStatus.REJECTED));
            requestRepository.saveAll(pendingRequests);
        }

        log.info("Request with id {} was confirmed", requestId);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto rejectRequestByInitiator(Long userId, Long eventId, Long requestId) {
        checkUserExist(userId);
        checkEventExist(eventId);
        checkRequestExist(requestId);

        ParticipationRequest request = requestRepository.getReferenceById(requestId);
        request.setStatus(RequestStatus.REJECTED);
        log.info("Request with id {} was rejected", requestId);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public EventFullDto findEventById(Long userId, Long eventId) {
        checkUserExist(userId);
        checkEventExist(eventId);
        Event event = eventRepository.getReferenceById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            log.info("Only event's initiator can see all information about event");
            throw new InvalidParameterException(String.format("Access error. " +
                    "User with id %d can't see full information about event with id %d", userId, eventId));
        }

        return EventMapper.toEventFullDto(eventRepository.getReferenceById(eventId));
    }

    @Override
    public List<EventShortDto> findAllEventsByUserId(Long userId, Integer from, Integer size) {
        checkUserExist(userId);
        return EventMapper.toEventShortDtos(eventRepository.findAllByInitiatorId(userId,
                PageRequest.of(from / size, size, Sort.by("id"))));
    }

    @Override
    public List<ParticipationRequestDto> findAllRequestsToEvent(Long userId, Long eventId) {
        checkUserExist(userId);
        checkEventExist(eventId);

        if (!eventRepository.getReferenceById(eventId).getInitiator().getId().equals(userId)) {
            log.info("Only initiator of event can see requests");
            throw new InvalidParameterException(String.format("Access error. " +
                    "User with id %d can't see requests to event with id %d", userId, eventId));
        }

        return RequestMapper.toParticipationRequestDtos(requestRepository.findAllByEventId(eventId));
    }

    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        checkUserExist(userId);
        checkEventExist(eventId);
        Event event = eventRepository.getReferenceById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            log.info("Only initiator can cancel event");
            throw new InvalidParameterException(String.format("Access error. " +
                    "User with id %d can't cancel event with id %d", userId, eventId));
        }
        if (!event.getState().equals(EventState.PENDING)) {
            log.info("Only events with pending state can be canceled");
            throw new InvalidParameterException(String.format("Incorrect event state. Event state " +
                    "have to be pending. Event with id %s has %s state", eventId, event.getState()));
        }

        event.setState(EventState.CANCELED);
        log.info("Event with id {} was canceled", eventId);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    private void checkUserExist(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.info("User with id {} wasn't found", userId);
            throw new ObjectNotFoundException(String.format("User with id %d wasn't found", userId));
        }
    }

    private void checkCategoryExist(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            log.info("Category with id {} wasn't found", categoryId);
            throw new ObjectNotFoundException(String.format("Category with id %d wasn't found", categoryId));
        }
    }

    private void checkEventExist(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.info("Event with id {} wasn't found", eventId);
            throw new ObjectNotFoundException(String.format("Event with id %d wasn't found", eventId));
        }
    }

    private void checkRequestExist(Long requestId) {
        if (!requestRepository.existsById(requestId)) {
            log.info("Request with id {} wasn't found", requestId);
            throw new ObjectNotFoundException(String.format("Request with id %d wasn't found", requestId));
        }
    }

    private void checkEventTimeIsCorrect(LocalDateTime time) {
        if (time.isBefore(LocalDateTime.now())) {
            log.info("Date can't be in the past");
            throw new InvalidParameterException("The date of event have to be in the future");
        }
    }
}