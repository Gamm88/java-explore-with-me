package ru.practicum.comment.service;

import ru.practicum.comment.model.*;

import java.util.List;

public interface CommentService {

    /**
     * Приватные сервисы, только для пользователей прошедших авторизацию.
     */

    // Добавление нового комментария.
    CommentDto addComment(Long userId, Long eventId, CommentNewDto commentNewDto);

    // Получение комментария по ИД.
    CommentDto getComment(Long commentId);

    // Удаление комментария.
    void deleteComment(Long userId, Long commentId);

    /**
     * Административные сервисы, только для администраторов.
     */

    // Получение всех комментариев пользователя.
    List<CommentDto> getAllCommentsByUserId(Long userId);

    // Получение всех комментариев события.
    List<CommentDto> getAllCommentsByEventId(Long eventId);

    // Редактирование комментария.
    CommentDto updateComment(Long userId, Long commentId, CommentNewDto commentNewDto);
}