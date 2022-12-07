package ru.practicum.explore.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.model.comment.Comment;
import ru.practicum.explore.model.comment.CommentState;
import ru.practicum.explore.model.event.EventState;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByCommentator_Id(Long commentatorId, Pageable pageable);

    List<Comment> findAllByEventIdAndCommentState(Long eventId, CommentState commentState, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE (c.commentator.id IN (?1) OR (?1) is null) " +
            "AND (c.commentState IN (?2) or (?2) is null)")
    List<Comment> findAllByParameters(List<Long> users, List<EventState> states, Pageable pageable);

}
