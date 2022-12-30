package ru.practicum.comment;

import ru.practicum.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Получение всех комментариев пользователя.
    List<Comment> getAllCommentsByUserId(Long userId);

    // Получение всех комментариев события.
    List<Comment> getAllCommentsByEventId(Long eventId);
}