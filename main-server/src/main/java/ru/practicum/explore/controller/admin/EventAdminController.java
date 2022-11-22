package ru.practicum.explore.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.event.EventFullDto;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.event.UpdateEventRequest;
import ru.practicum.explore.service.admin.EventAdminService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventAdminService eventService;

    @PutMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        return eventService.updateEvent(eventId, updateEventRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        return eventService.rejectEvent(eventId);
    }

    @GetMapping
    public List<EventFullDto> findAllEvents(@RequestParam List<Long> users,
                                            @RequestParam(defaultValue = "PUBLISHED,PENDING, CANCELED")
                                            List<EventState> states,
                                            @RequestParam List<Long> categories,
                                            @RequestParam(defaultValue = "null") String rangeStart,
                                            @RequestParam(defaultValue = "null") String rangeEnd,
                                            @RequestParam(required = false, defaultValue = "0") Integer from,
                                            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.findAllEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}