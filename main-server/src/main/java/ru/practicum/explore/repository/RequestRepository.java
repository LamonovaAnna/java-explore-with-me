package ru.practicum.explore.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.request.ParticipationRequest;
import ru.practicum.explore.model.request.RequestStatus;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByEventIdAndStatus(Long eventId, RequestStatus status);

    List<ParticipationRequest> findAllByRequesterId(Long userId);

    List<ParticipationRequest> findAllByRequesterIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

}