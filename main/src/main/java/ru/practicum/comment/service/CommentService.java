package ru.practicum.comment.service;

import ru.practicum.comment.model.*;

import java.util.List;

public interface CommentService {

    // Публичные эндпоинты

    // Получение комментария по ИД.
    CommentDto getComment(Long commentId);

    // Получение всех комментариев мероприятия.
    List<CommentDto> getAllCommentsByEventId(Long eventId);

    // Приватные сервисы, только для пользователей прошедших авторизацию:

    // Добавление нового комментария.
    CommentDto addComment(Long userId, Long eventId, CommentNewDto commentNewDto);

    // Получение всех комментариев пользователя.
    List<CommentDto> getAllCommentsByUserId(Long userId);

    // Редактирование комментария.
    CommentDto updateComment(Long userId, Long commentId, CommentNewDto commentNewDto);

    // Удаление комментария.
    void deleteComment(Long userId, Long commentId);
}