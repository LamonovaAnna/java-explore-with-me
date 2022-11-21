package ru.practicum.explore.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.request.ParticipationRequest;
import ru.practicum.explore.model.request.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllRequestsByEventIdAndStatus(Long eventId, RequestStatus status);

    List<ParticipationRequest> findAllRequestsByRequesterId(Long userId);

    List<ParticipationRequest> findAllRequestsByRequesterIdAndEventId(Long userId, Long eventId);

}