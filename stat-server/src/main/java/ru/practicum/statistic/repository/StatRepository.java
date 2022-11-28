package ru.practicum.statistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statistic.model.EndpointHit;
import ru.practicum.statistic.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.statistic.model.ViewStats(h.app, h.uri, CAST(COUNT (h.id) AS int)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp >= :start AND h.timestamp <= :end " +
            "GROUP BY h.app, h.uri")
    List<ViewStats> findByStartAndEndTime(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.statistic.model.ViewStats(h.app, h.uri, CAST(COUNT (h.id) AS int)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp >= :start AND h.timestamp <= :end " +
            "GROUP BY h.app, h.uri, h.ip")
    List<ViewStats> findByStartAndEndTimeUrisIsNotUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.statistic.model.ViewStats(h.app, h.uri, CAST(COUNT (h.id) AS int)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp >= :start AND h.timestamp <= :end AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri, h.ip")
    List<ViewStats> findByStartAndEndTimeWithUrisIsUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.statistic.model.ViewStats(h.app, h.uri, CAST(COUNT (h.id) AS int)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp >= :start AND h.timestamp <= :end AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri")
    List<ViewStats> findByStartAndEndTimeWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}