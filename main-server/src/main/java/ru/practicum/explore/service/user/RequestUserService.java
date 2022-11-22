package ru.practicum.explore.service.user;

import ru.practicum.explore.model.request.ParticipationRequestDto;

import java.util.List;

public interface RequestUserService {
    List<ParticipationRequestDto> findAllRequests(Long userId);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
