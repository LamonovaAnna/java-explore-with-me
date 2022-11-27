package ru.practicum.explore.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;


import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    Event findByIdAndState(Long eventId, EventState state);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE (e.initiator.id IN (?1) OR (?1) is null) " +
            "AND (e.category.id IN (?2) or (?2) is null) AND (e.state IN (?3) or (?3) is null) " +
            "AND e.eventDate > ?4 AND e.eventDate < ?5")
    List<Event> findAllByParameters(List<Long> users, List<Long> categories, List<EventState> states,
                                       LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (UPPER(e.annotation) LIKE UPPER(CONCAT('%',:text,'%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%',:text,'%')) " +
            "OR :text IS null) " +
            "AND (e.category.id IN (:categories) OR :categories IS null) " +
            "AND (e.paid = :paid OR :paid IS null ) " +
            "AND (e.eventDate > :start) " +
            "AND (e.eventDate < :end)")
    List<Event> getFilteredEvents(String text, List<Long> categories, Boolean paid, LocalDateTime start,
                                  LocalDateTime end, Pageable pageable);
}