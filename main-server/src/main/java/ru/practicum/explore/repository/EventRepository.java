package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.event.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
