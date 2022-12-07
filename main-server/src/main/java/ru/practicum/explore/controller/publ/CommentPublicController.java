package ru.practicum.explore.controller.publ;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.comment.CommentDto;
import ru.practicum.explore.model.comment.CommentShortDto;
import ru.practicum.explore.service.publ.CommentPublicService;

import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/comments")
@RequiredArgsConstructor
public class CommentPublicController {
    private final CommentPublicService commentService;

    @GetMapping()
    public List<CommentShortDto> getAllCommentsByEvent(@PathVariable Long eventId,
                                                           @RequestParam(required = false, defaultValue = "0") Integer from,
                                                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        return commentService.getAllCommentsByEvent(eventId, from, size);
    }
}
