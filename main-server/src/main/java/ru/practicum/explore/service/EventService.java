package ru.practicum.explore.service;

import ru.practicum.explore.model.event.EventFullDto;
import ru.practicum.explore.model.event.UpdateEventRequest;

public interface EventService {
    EventFullDto createEvent(Long userId, EventFullDto eventFullDto);

    EventFullDto updateEventByUser(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventRequest updateEventRequest);

    EventFullDto publishEvent(Long eventId);

    EventFullDto rejectEvent(Long eventId);

}
