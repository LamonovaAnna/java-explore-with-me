package ru.practicum.explore.model.comment;

import lombok.Data;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "content", nullable = false)
    private String text;

    @ManyToOne
    private User commentator;

    @ManyToOne
    private Event event;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CommentState commentState;

    @Column(name = "added", nullable = false)
    private LocalDateTime added = LocalDateTime.now();
}
