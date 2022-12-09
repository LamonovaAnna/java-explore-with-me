package ru.practicum.explore.service.user;

import ru.practicum.explore.model.comment.CommentDto;

import java.util.List;

public interface CommentUserService {

    CommentDto createComment(Long userId, Long eventId, CommentDto commentDto);

    CommentDto updateComment(Long userId, CommentDto commentDto);

    void deleteComment(Long userId, Long commentId);

    CommentDto getCommentById(Long userId, Long commentId);

    List<CommentDto> getAllComments(Long userId, Integer from, Integer size);
}
