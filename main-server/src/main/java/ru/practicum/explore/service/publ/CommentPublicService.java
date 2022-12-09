package ru.practicum.explore.service.publ;

import ru.practicum.explore.model.comment.CommentShortDto;

import java.util.List;

public interface CommentPublicService {

    List<CommentShortDto> getAllCommentsByEvent(Long eventId, Integer from, Integer size);

}
