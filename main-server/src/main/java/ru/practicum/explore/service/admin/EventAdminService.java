package ru.practicum.explore.service.admin;

import ru.practicum.explore.model.event.EventFullDto;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.event.UpdateEventRequest;

import java.util.List;

public interface EventAdminService {

    EventFullDto updateEvent(Long eventId, UpdateEventRequest updateEventRequest);

    EventFullDto publishEvent(Long eventId);

    EventFullDto rejectEvent(Long eventId);

    List<EventFullDto> findAllEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                     String rangeStart, String rangeEnd, Integer from, Integer size);

}
