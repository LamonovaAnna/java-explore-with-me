package ru.practicum.explore.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;


import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (e.initiator.id IN (?1) OR (?1) is null) " +
            "AND (e.category.id IN (?2) or (?2) is null) " +
            "AND (e.state IN (?3) or (?3) is null) " +
            "AND e.eventDate > ?4 AND e.eventDate < ?5 " +
            "AND e.state = 'PUBLISHED'")
    List<Event> findAllByParameters(List<Long> users, List<Long> categories, List<EventState> states,
                                       LocalDateTime start, LocalDateTime end, Pageable pageable);

}
