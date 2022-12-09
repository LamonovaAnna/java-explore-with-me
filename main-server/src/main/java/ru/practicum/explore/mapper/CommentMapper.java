package ru.practicum.explore.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explore.model.comment.Comment;
import ru.practicum.explore.model.comment.CommentDto;
import ru.practicum.explore.model.comment.CommentShortDto;
import ru.practicum.explore.model.comment.CommentState;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.user.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static Comment toComment(CommentDto commentDto, Long userId, Event event) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setCommentator(new User(userId, null, null));
        comment.setEvent(event);
        comment.setCommentState(CommentState.PENDING);
        return comment;
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getCommentator().getId(),
                comment.getEvent().getId(),
                comment.getCommentState(),
                comment.getAdded());
    }

    public static CommentShortDto toCommentShortDto(Comment comment) {
        return new CommentShortDto(comment.getId(),
                comment.getText(),
                comment.getCommentator().getId(),
                comment.getAdded());
    }

    public static Comment toUpdateComment(Comment comment, CommentDto updateComment) {
        comment.setText(updateComment.getText() == null ? comment.getText() : updateComment.getText());
        return comment;
    }

    public static List<CommentDto> toCommentDtos(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    public static List<CommentShortDto> toCommentShortDtos(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentShortDto).collect(Collectors.toList());
    }
}
