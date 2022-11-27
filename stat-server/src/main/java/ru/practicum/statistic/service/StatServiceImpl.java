package ru.practicum.statistic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.statistic.mapper.EndpointHitMapper;
import ru.practicum.statistic.model.EndpointHitDto;
import ru.practicum.statistic.model.ViewStats;
import ru.practicum.statistic.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Override
    public void createHit(EndpointHitDto endpointHitDto) {
        statRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
    }

    @Override
    public List<ViewStats> getAllHits(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<ViewStats> statistic;

        if (uris.isEmpty()) {
            if (unique) {
                statistic = statRepository.findByStartAndEndTime(start, end);
            } else {
                statistic = statRepository.findByStartAndEndTimeUrisIsNotUnique(start, end);
            }
        } else {
            if (unique) {
                statistic = statRepository.findByStartAndEndTimeWithUrisIsUnique(start, end, uris);
            } else {
                statistic = statRepository.findByStartAndEndTimeWithUris(start, end, uris);
            }
        }
        return statistic;
    }
}