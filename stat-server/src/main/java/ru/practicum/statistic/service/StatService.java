package ru.practicum.statistic.service;

import ru.practicum.statistic.model.EndpointHitDto;
import ru.practicum.statistic.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    void createHit(EndpointHitDto endpointHitDto);

    List<ViewStats> getAllHits(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
