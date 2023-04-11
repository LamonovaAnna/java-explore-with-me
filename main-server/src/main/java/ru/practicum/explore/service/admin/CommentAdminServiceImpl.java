package ru.practicum.explore.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.InvalidParameterException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.mapper.CommentMapper;
import ru.practicum.explore.model.comment.Comment;
import ru.practicum.explore.model.comment.CommentDto;
import ru.practicum.explore.model.comment.CommentState;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentAdminServiceImpl implements CommentAdminService {
    private final CommentRepository commentRepository;

    @Override
    public CommentDto publishComment(Long commentId) {
        checkCommentExist(commentId);
        Comment comment = commentRepository.getReferenceById(commentId);

        if (!comment.getCommentState().equals(CommentState.PENDING)) {
            log.error("Comment with id {} can't be published. State have to be pending", commentId);
            throw new InvalidParameterException("Comment should have PENDING state");
        }

        comment.setCommentState(CommentState.PUBLISHED);
        log.info("Comment with id {} was published", commentId);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto rejectComment(Long commentId) {
        checkCommentExist(commentId);
        Comment comment = commentRepository.getReferenceById(commentId);

        if (!comment.getCommentState().equals(CommentState.PENDING)) {
            log.error("Comment with id {} can't be published. State have to be pending", commentId);
            throw new InvalidParameterException("Comment should have PENDING state");
        }

        comment.setCommentState(CommentState.REJECTED);
        log.info("Comment with id {} was published", commentId);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> findAllComments(List<Long> users, List<EventState> states, Integer from, Integer size) {
        return CommentMapper.toCommentDtos(commentRepository.findAllByParameters(users, states,
                PageRequest.of(from / size, size, Sort.by("id"))));
    }

    private void checkCommentExist(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            log.error("Comment with id {} wasn't found", commentId);
            throw new ObjectNotFoundException(String.format("Comment with id %d wasn't found", commentId));
        }
    }
}