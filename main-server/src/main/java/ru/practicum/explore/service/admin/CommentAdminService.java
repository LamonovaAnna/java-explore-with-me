package ru.practicum.explore.service.admin;

import ru.practicum.explore.model.comment.CommentDto;
import ru.practicum.explore.model.event.EventState;

import java.util.List;

public interface CommentAdminService {

    CommentDto publishComment(Long commentId);

    CommentDto rejectComment(Long commentId);

    List<CommentDto> findAllComments(List<Long> users, List<EventState> states, Integer from, Integer size);
}
