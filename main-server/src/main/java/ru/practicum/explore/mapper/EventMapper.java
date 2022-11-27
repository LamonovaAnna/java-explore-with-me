package ru.practicum.explore.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.event.*;
import ru.practicum.explore.model.location.Location;
import ru.practicum.explore.model.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()),
                new Location(event.getLat(), event.getLon()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews());
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews());
    }

    public static Event toEventFromNew(NewEventDto eventDto, Long userId, Category category) {
        Event event = new Event();
        event.setAnnotation(eventDto.getAnnotation());
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(eventDto.getDescription());
        event.setEventDate(eventDto.getEventDate());
        event.setInitiator(new User(userId, null, null));
        event.setLon(eventDto.getLocation().getLon());
        event.setLat(eventDto.getLocation().getLat());
        event.setPaid(eventDto.getPaid() != null ? eventDto.getPaid() : null);
        event.setParticipantLimit(eventDto.getParticipantLimit() != null ? eventDto.getParticipantLimit() : null);
        event.setRequestModeration(eventDto.getRequestModeration()!= null ? eventDto.getRequestModeration() : null);
        event.setTitle(eventDto.getTitle());
        event.setState(EventState.PENDING);
        return event;
    }

    public static Event toEventFromUpdateEventRequest(UpdateEventRequest eventDto) {
        Event event = new Event();
        event.setId(eventDto.getEventId());
        event.setAnnotation(eventDto.getAnnotation() != null ? eventDto.getAnnotation() : null);
        event.setCategory(new Category(eventDto.getCategoryId(), null));
        event.setDescription(eventDto.getDescription() != null ? eventDto.getDescription() : null);
        event.setEventDate(eventDto.getEventDate() != null ? eventDto.getEventDate() : null);
        event.setPaid(eventDto.getPaid() != null ? eventDto.getPaid() : null);
        event.setParticipantLimit(eventDto.getParticipantLimit() != null ? eventDto.getParticipantLimit() : null);
        event.setTitle(eventDto.getTitle() != null ? eventDto.getTitle() : null);
        return event;
    }

    public static Event toUpdateEvent(Event event, Event updateEvent) {
        event.setAnnotation(updateEvent.getAnnotation() == null ? event.getAnnotation() : updateEvent.getAnnotation());
        event.setPaid(updateEvent.getPaid() == null ? event.getPaid() : updateEvent.getPaid());
        event.setParticipantLimit(updateEvent.getParticipantLimit() == null ?
                event.getParticipantLimit() : updateEvent.getParticipantLimit());
        event.setEventDate(updateEvent.getEventDate() == null ? event.getEventDate() : updateEvent.getEventDate());
        event.setDescription(updateEvent.getDescription() == null ?
                event.getDescription() : updateEvent.getDescription());
        event.setCategory(updateEvent.getCategory() == null ? event.getCategory() : updateEvent.getCategory());
        event.setTitle(updateEvent.getTitle() == null ? event.getTitle() : updateEvent.getTitle());
        return event;
    }

    public static List<EventShortDto> toEventShortDtos(List<Event> events) {
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public static List<EventFullDto> toEventFullDtos(List<Event> events) {
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }
}