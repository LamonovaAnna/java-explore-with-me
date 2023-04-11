package ru.practicum.explore.service.user;

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
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.repository.CommentRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentUserServiceImpl implements CommentUserService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public CommentDto createComment(Long userId, Long eventId, CommentDto commentDto) {
        checkUserExist(userId);
        checkEventExist(eventId);
        Event event = eventRepository.getReferenceById(eventId);

        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(commentDto, userId, event)));
    }

    public CommentDto updateComment(Long userId, CommentDto commentDto) {
        checkUserExist(userId);
        checkEventExist(commentDto.getEventId());
        checkCommentExist(commentDto.getId());
        Comment comment = commentRepository.getReferenceById(commentDto.getId());
        checkUserIsCommentOwner(userId, comment);

        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toUpdateComment(comment, commentDto)));
    }

    public void deleteComment(Long userId, Long commentId) {
        checkCommentExist(commentId);
        Comment comment = commentRepository.getReferenceById(commentId);
        checkUserIsCommentOwner(userId, comment);
        commentRepository.deleteById(commentId);
    }

    public CommentDto getCommentById(Long userId, Long commentId) {
        checkUserExist(userId);
        checkCommentExist(commentId);
        Comment comment = commentRepository.getReferenceById(commentId);
        checkUserIsCommentOwner(userId, comment);

        return CommentMapper.toCommentDto(comment);
    }

    public List<CommentDto> getAllComments(Long userId, Integer from, Integer size) {
        return CommentMapper.toCommentDtos(commentRepository.findAllByCommentator_Id(userId,
                PageRequest.of(from / size, size, Sort.by("id"))));
    }

    private void checkEventExist(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.error("Event with id {} wasn't found", eventId);
            throw new ObjectNotFoundException(String.format("Event with id %d wasn't found", eventId));
        }
    }

    private void checkUserExist(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.error("User with id {} wasn't found", userId);
            throw new ObjectNotFoundException(String.format("User with id %d wasn't found", userId));
        }
    }

    private void checkCommentExist(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            log.error("Comment with id {} wasn't found", commentId);
            throw new ObjectNotFoundException(String.format("Comment with id %d wasn't found", commentId));
        }
    }

    private void checkUserIsCommentOwner(Long userId, Comment comment) {
        if (!comment.getCommentator().getId().equals(userId)) {
            log.error("Only owner can update his own comment with id {}", comment.getId());
            throw new InvalidParameterException(String.format("Access error. Only commentator can update his own " +
                    "comment with id %d", comment.getId()));
        }
    }
}
