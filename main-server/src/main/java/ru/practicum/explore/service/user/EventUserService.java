package ru.practicum.explore.service.user;

import ru.practicum.explore.model.event.EventFullDto;
import ru.practicum.explore.model.event.EventShortDto;
import ru.practicum.explore.model.event.NewEventDto;
import ru.practicum.explore.model.event.UpdateEventRequest;
import ru.practicum.explore.model.request.ParticipationRequestDto;

import java.util.List;

public interface EventUserService {

    EventFullDto createEvent(Long userId, NewEventDto eventDto);

    EventFullDto updateEvent(Long userId, UpdateEventRequest updateEventRequest);

    ParticipationRequestDto confirmRequestByInitiator(Long userId, Long eventId, Long requestId);

    ParticipationRequestDto rejectRequestByInitiator(Long userId, Long eventId, Long requestId);

    EventFullDto findEventById(Long userId, Long eventId);

    List<EventShortDto> findAllEventsByUserId(Long userId, Integer from, Integer size);

    List<ParticipationRequestDto> findAllRequestsToEvent(Long userId, Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);

}