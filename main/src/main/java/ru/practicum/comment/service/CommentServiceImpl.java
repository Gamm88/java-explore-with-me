package ru.practicum.comment.service;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.comment.model.*;
import ru.practicum.user.model.User;
import lombok.RequiredArgsConstructor;
import ru.practicum.event.model.event.Event;
import ru.practicum.user.service.UserService;
import org.springframework.stereotype.Service;
import ru.practicum.comment.CommentRepository;
import ru.practicum.event.service.EventService;
import ru.practicum.exeptions.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final EventService eventService;
    private final CommentRepository commentRepository;

    /**
     * Приватные сервисы, только для пользователей прошедших авторизацию.
     */

    // Добавление нового комментария.
    @Override
    public CommentDto addComment(Long userId, Long eventId, CommentNewDto commentNewDto) {
        User user = userService.getUserOrNotFound(userId);
        Event event = eventService.getEventOrNotFound(eventId);
        Comment comment = CommentMapper.mapToComment(user, event, commentNewDto);
        commentRepository.save(comment);
        log.info("CommentService - добавлен новый комментарий: {}.", comment);

        return CommentMapper.mapToCommentDto(comment);
    }

    // Получение комментария по ИД.
    @Override
    public CommentDto getComment(Long commentId) {
        Comment comment = getCommentOrNotFound(commentId);
        log.info("CommentService - предоставлен комментарий: {}.", comment);

        return CommentMapper.mapToCommentDto(comment);
    }

    // Удаление комментария.
    @Override
    public void deleteComment(Long userId, Long commentId) {
        userService.getUserOrNotFound(userId);
        getCommentOrNotFound(commentId);
        commentRepository.deleteById(commentId);
        log.info("CommentService - удалён комментарий c ИД: {}.", commentId);
    }

    /**
     * Административные сервисы, только для администраторов.
     */

    // Получение всех комментариев пользователя.
    @Override
    public List<CommentDto> getAllCommentsByUserId(Long userId) {
        userService.getUserOrNotFound(userId);
        List<Comment> comments = commentRepository.getAllCommentsByUserId(userId);
        log.info("CommentService - предоставлены комментарии: {}.", comments);

        return CommentMapper.mapToCommentDto(comments);
    }

    // Получение всех комментариев события.
    @Override
    public List<CommentDto> getAllCommentsByEventId(Long eventId) {
        eventService.getEventOrNotFound(eventId);
        List<Comment> comments = commentRepository.getAllCommentsByEventId(eventId);
        log.info("CommentService - предоставлены комментарии: {}.", comments);

        return CommentMapper.mapToCommentDto(comments);
    }

    // Редактирование комментария.
    @Override
    public CommentDto updateComment(Long userId, Long commentId, CommentNewDto commentNewDto) {
        userService.getUserOrNotFound(userId);
        Comment comment = getCommentOrNotFound(commentId);
        comment.setText(commentNewDto.getText());
        commentRepository.save(comment);
        log.info("CommentService - изменён текст комментария: {}.", comment);

        return CommentMapper.mapToCommentDto(comment);
    }

    /**
     * Вспомогательные методы.
     */

    // Получение комментария, если не найдена - ошибка 404.
    private Comment getCommentOrNotFound(Long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с ИД: " + commentId + ", не найден!"));
    }
}