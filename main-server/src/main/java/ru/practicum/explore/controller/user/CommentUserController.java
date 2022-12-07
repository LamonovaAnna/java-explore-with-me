package ru.practicum.explore.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.comment.CommentDto;
import ru.practicum.explore.service.user.CommentUserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentUserController {
    private final CommentUserService commentService;

    @PostMapping("/events/{eventId}")
    public CommentDto createComment(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid CommentDto commentDto) {
        return commentService.createComment(userId, eventId, commentDto);
    }

    @PatchMapping()
    public CommentDto updateComment(@PathVariable Long userId,
                                    @RequestBody @Valid CommentDto commentDto) {
        return commentService.updateComment(userId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@PathVariable Long userId,
                                     @PathVariable Long commentId) {
        return commentService.getCommentById(userId, commentId);
    }

    @GetMapping("/{userId}/comments")
    public List<CommentDto> getAllComments(@PathVariable Long userId,
                                           @RequestParam(required = false, defaultValue = "0") Integer from,
                                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        return commentService.getAllComments(userId, from, size);
    }
}