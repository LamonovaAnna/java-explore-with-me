package ru.practicum.explore.service.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.client.StatisticClient;
import ru.practicum.explore.exception.InvalidParameterException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventFullDto;
import ru.practicum.explore.model.event.EventShortDto;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.hit.EndpointHitDto;
import ru.practicum.explore.repository.EventRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublicServiceImpl implements EventPublicService {
    private final EventRepository eventRepository;
    private final StatisticClient statisticClient;

    @Override
    public EventFullDto findEventById(Long eventId, HttpServletRequest request) {
        checkEventExist(eventId);
        Event event = eventRepository.getReferenceById(eventId);
        sentHitToStatistic(request);

        if (event.getViews() == null) {
            event.setViews(1);
        } else {
            event.setViews(event.getViews() + 1);
        }

        eventRepository.save(event);

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                         String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                         Integer size, HttpServletRequest request) {
        String sorting;
        switch (sort) {
            case "EVENT_DATE":
                sorting = "eventDate";
                break;
            case "VIEWS":
                sorting = "views";
                break;
            case "id":
                sorting = "id";
                break;
            default:
                log.info("Не существующий тип сортировки {}", sort);
                throw new InvalidParameterException(String.format("Не существующий тип сортировки %s", sort));
        }
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(sorting));


        LocalDateTime startDate;
        if (rangeStart != null) {
            startDate = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            startDate = LocalDateTime.now();
        }

        LocalDateTime endDate;
        if (rangeEnd == null) {
            endDate = LocalDateTime.now().plusYears(50);
        } else {
            endDate = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        List<Event> sortedEvents = eventRepository.getFilteredEvents(text, categories,
                paid, startDate, endDate, pageable);

        if (onlyAvailable != null && onlyAvailable) {
            sortedEvents.removeIf(event -> event.getConfirmedRequests() == event.getParticipantLimit().longValue());
        }

        sentHitToStatistic(request);
        return EventMapper.toEventShortDtos(sortedEvents);
    }

    private void sentHitToStatistic(HttpServletRequest request) {
        statisticClient.createHit(EndpointHitDto.builder()
                .app("mainApp")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }

    private void checkEventExist(Long eventId) {
        if (eventRepository.findByIdAndState(eventId, EventState.PUBLISHED) == null) {
            log.info("Event with id {} wasn't found", eventId);
            throw new ObjectNotFoundException(String.format("Event with id %d wasn't found", eventId));
        }
    }
}