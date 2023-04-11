package ru.practicum.explore.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.comment.CommentDto;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.service.admin.CommentAdminService;

import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class CommentAdminController {
    private final CommentAdminService commentService;

    @PatchMapping("/{commentId}/publish")
    public CommentDto publishComment(@PathVariable Long commentId) {
        return commentService.publishComment(commentId);
    }

    @PatchMapping("/{commentId}/reject")
    public CommentDto rejectComment(@PathVariable Long commentId) {
        return commentService.rejectComment(commentId);
    }

    @GetMapping
    public List<CommentDto> findAllComments(@RequestParam(required = false) List<Long> users,
                                            @RequestParam(required = false,
                                                    defaultValue = "PUBLISHED, PENDING, REJECTED")
                                            List<EventState> states,
                                            @RequestParam(required = false, defaultValue = "0") Integer from,
                                            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return commentService.findAllComments(users, states, from, size);
    }
}
