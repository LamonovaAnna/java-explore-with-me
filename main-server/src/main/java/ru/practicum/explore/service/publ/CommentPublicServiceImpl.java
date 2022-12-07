package ru.practicum.explore.service.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.mapper.CommentMapper;
import ru.practicum.explore.model.comment.CommentDto;
import ru.practicum.explore.model.comment.CommentShortDto;
import ru.practicum.explore.model.comment.CommentState;
import ru.practicum.explore.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentPublicServiceImpl implements CommentPublicService{
    private final CommentRepository commentRepository;

    @Override
    public List<CommentShortDto> getAllCommentsByEvent(Long eventId, Integer from, Integer size) {
        return CommentMapper.toCommentShortDtos(commentRepository.findAllByEventIdAndCommentState(eventId,
                CommentState.PUBLISHED,
                PageRequest.of(from / size, size, Sort.by("added").descending())));
    }
}
