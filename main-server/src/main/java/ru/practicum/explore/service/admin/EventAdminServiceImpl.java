package ru.practicum.explore.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.InvalidParameterException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.mapper.CommentMapper;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.comment.CommentState;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventFullDto;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.event.UpdateEventRequest;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.CommentRepository;
import ru.practicum.explore.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventAdminServiceImpl implements EventAdminService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventRequest updateEventRequest) {
        checkEventExist(eventId);
        Event event = eventRepository.getReferenceById(eventId);

        Category category = null;
        if (updateEventRequest.getCategoryId() != null) {
            checkCategoryExist(updateEventRequest.getCategoryId());
            category = categoryRepository.getReferenceById(updateEventRequest.getCategoryId());
        }

        log.info("Event with id {} was updated", updateEventRequest.getEventId());
        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.toUpdateEvent(
                event, EventMapper.toEventFromUpdateEventRequest(updateEventRequest, category))));
    }

    @Override
    public EventFullDto publishEvent(Long eventId) {
        checkEventExist(eventId);
        Event event = eventRepository.getReferenceById(eventId);

        if (!event.getState().equals(EventState.PENDING)) {
            log.error("Event with id {} can't be published. State have to be pending", eventId);
            throw new InvalidParameterException("Event should have PENDING state");
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
            log.error("Event with id {} can't be published. Start time have to be more then 2 hour before the event",
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
            log.error("Event with id {} is already published", eventId);
            throw new InvalidParameterException(String.format("Event with id %s is already published", eventId));
        }

        event.setState(EventState.CANCELED);
        log.info("Event with id {} was canceled", eventId);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> findAllEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                            String rangeStart, String rangeEnd, Integer from, Integer size) {
        LocalDateTime startTime = rangeStart != null ? LocalDateTime.parse(rangeStart,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : LocalDateTime.now();

        LocalDateTime endTime = rangeEnd != null ? LocalDateTime.parse(rangeEnd,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : LocalDateTime.now().plusYears(100);

        List<EventFullDto> events = EventMapper.toEventFullDtos(eventRepository.findAllByParameters(
                users, categories, states, startTime, endTime,
                PageRequest.of(from / size, size, Sort.by("id"))));
        events.forEach(e -> e.setComments(CommentMapper.toCommentShortDtos(
                commentRepository.findAllByEvent_IdAndCommentState(e.getId(), CommentState.PUBLISHED,
                        Sort.by("added").descending()))));

        return events;
    }

    private void checkEventExist(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.error("Event with id {} wasn't found", eventId);
            throw new ObjectNotFoundException(String.format("Event with id %d wasn't found", eventId));
        }
    }

    private void checkCategoryExist(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            log.error("Category with id {} wasn't found", categoryId);
            throw new ObjectNotFoundException(String.format("Category with id %d wasn't found", categoryId));
        }
    }
}