package ru.practicum.explore.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.event.EventFullDto;
import ru.practicum.explore.model.event.EventShortDto;
import ru.practicum.explore.model.event.NewEventDto;
import ru.practicum.explore.model.event.UpdateEventRequest;
import ru.practicum.explore.model.request.ParticipationRequestDto;
import ru.practicum.explore.service.user.EventUserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventUserController {
    private final EventUserService eventService;

    @PostMapping
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody @Valid NewEventDto eventDto) {
        return eventService.createEvent(userId, eventDto);
    }

    @PatchMapping
    public EventFullDto patchEvent(@PathVariable Long userId,
                                   @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        return eventService.updateEvent(userId, updateEventRequest);
    }

    @PatchMapping("/{eventId}/requests/{requestId}/confirm")
    public ParticipationRequestDto confirmAnotherRequestToUsersEvent(@PathVariable Long userId,
                                                                     @PathVariable Long eventId,
                                                                     @PathVariable Long requestId) {
        return eventService.confirmRequestByInitiator(userId, eventId, requestId);
    }

    @PatchMapping("/{eventId}/requests/{requestId}/reject")
    public ParticipationRequestDto rejectAnotherRequestToUsersEvent(@PathVariable Long userId,
                                                                    @PathVariable Long eventId,
                                                                    @PathVariable Long requestId) {
        return eventService.rejectRequestByInitiator(userId, eventId, requestId);
    }

    @GetMapping("/{eventId}")
    public EventFullDto findByEventId(@PathVariable Long userId,
                                      @PathVariable Long eventId) {
        return eventService.findEventById(userId, eventId);
    }

    @GetMapping
    public List<EventShortDto> findAllEventsByUserId(@PathVariable Long userId,
                                                     @RequestParam(required = false, defaultValue = "0") Integer from,
                                                     @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.findAllEventsByUserId(userId, from, size);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findAllRequestsToEvent(@PathVariable Long userId,
                                                                @PathVariable Long eventId) {
        return eventService.findAllRequestsToEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId) {
        return eventService.cancelEvent(userId, eventId);
    }
}