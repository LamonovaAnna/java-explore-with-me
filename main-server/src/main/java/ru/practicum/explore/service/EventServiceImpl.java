package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.InvalidParameterException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventFullDto;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.event.UpdateEventRequest;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.RequestRepository;
import ru.practicum.explore.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    //private final RequestRepository requestRepository;

    @Override
    public EventFullDto createEvent(Long userId, EventFullDto eventFullDto) {
        checkUserExist(userId);
        checkCategoryExist(eventFullDto.getCategory().getId());
        checkEventTimeIsCorrect(eventFullDto.getEventDate());
        log.info("New event was created by initiator id {}", userId);
        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.toEvent(eventFullDto)));
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, UpdateEventRequest updateEventRequest) {
        checkUserExist(userId);
        checkEventExist(updateEventRequest.getEventId());
        if (updateEventRequest.getCategoryId() != null) {
            checkCategoryExist(updateEventRequest.getCategoryId());
        }
        if (updateEventRequest.getEventDate() != null) {
            checkEventTimeIsCorrect(updateEventRequest.getEventDate());
        }

        Event event = eventRepository.findById(updateEventRequest.getEventId()).get();
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
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventRequest updateEventRequest) {
        checkEventExist(eventId);
        Event event = eventRepository.findById(updateEventRequest.getEventId()).get();
        log.info("Event with id {} was updated", updateEventRequest.getEventId());
        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.toUpdateEvent(
                event, EventMapper.toEventFromUpdateEventRequest(updateEventRequest))));
    }

    @Override
    public EventFullDto publishEvent(Long eventId) {
        checkEventExist(eventId);
        Event event = eventRepository.findById(eventId).get();

        if (!event.getState().equals(EventState.PENDING)) {
            log.info("Event with id {} can't be published. State have to be pending", eventId);
            throw new InvalidParameterException("Event should have PENDING state");
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
            log.info("Event with id {} can't be published. Start time have to be more then 2 hour before the event",
                    eventId);
            throw new InvalidParameterException("Start time have to be more then 2 hour before the event");
        }

        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED);
        log.info("Event with id {} was published", eventId);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        checkEventExist(eventId);
        Event event = eventRepository.getReferenceById(eventId);

        if (event.getState().equals(EventState.PUBLISHED)) {
            log.info("Event with id {} is already published", eventId);
            throw new InvalidParameterException(String.format("Event with id %s is already published", eventId));
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

    private void checkEventTimeIsCorrect(LocalDateTime time) {
        if (time.isBefore(LocalDateTime.now())) {
            log.info("Date can't be in the past");
            throw new InvalidParameterException("The date of event have to be in the future");
        }
    }

    private void checkEventExist(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.info("Event with id {} wasn't found", eventId);
            throw new ObjectNotFoundException(String.format("Event with id %d wasn't found", eventId));
        }
    }
}