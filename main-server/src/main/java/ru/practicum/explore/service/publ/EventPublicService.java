package ru.practicum.explore.service.publ;

import ru.practicum.explore.model.event.EventFullDto;
import ru.practicum.explore.model.event.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventPublicService {

    EventFullDto findEventById(Long eventId, HttpServletRequest request);

    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                  String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                  String sort, Integer from, Integer size,
                                  HttpServletRequest request);
}
