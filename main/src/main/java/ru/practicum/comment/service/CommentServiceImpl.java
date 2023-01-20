package ru.practicum.comment.service;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.comment.model.*;
import ru.practicum.user.model.Ranking;
import ru.practicum.user.model.User;
import lombok.RequiredArgsConstructor;
import ru.practicum.event.model.event.Event;
import ru.practicum.user.service.UserService;
import org.springframework.stereotype.Service;
import ru.practicum.event.service.EventService;
import ru.practicum.exeptions.NotFoundException;
import ru.practicum.exeptions.ValidatorExceptions;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.comment.repository.CommentRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final EventService eventService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    // Публичные эндпоинты:

    // Получение комментария по ИД.
    @Override
    public CommentDto getComment(Long commentId) {
        Comment comment = getCommentOrNotFound(commentId);
        log.info("CommentService - предоставлен комментарий: {}.", comment);

        return CommentMapper.mapToCommentDto(comment);
    }

    // Получение всех комментариев мероприятия.
    @Override
    public List<CommentDto> getAllCommentsByEventId(Long eventId) {
        eventService.getEventOrNotFound(eventId);
        List<Comment> comments = commentRepository.getAllCommentsByEventId(eventId);
        log.info("CommentService - предоставлены комментарии: {}.", comments);

        return CommentMapper.mapToCommentDto(comments);
    }

    // Приватные сервисы, только для пользователей прошедших авторизацию:

    // Добавление нового комментария.
    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long eventId, CommentNewDto commentNewDto) {
        User user = userService.getUserOrNotFound(userId);
        if (user.isCommentsBan()) {
            throw new ValidatorExceptions("Для вашего пользователя комментарии недоступны!");
        }
        Event event = eventService.getEventOrNotFound(eventId);
        Comment comment = CommentMapper.mapToComment(user, event, commentNewDto);
        commentRepository.save(comment);
        log.info("CommentService - добавлен новый комментарий: {}.", comment);

        // Обновляем информацию по пользователю (количество комментариев и рейтинг).
        user.setCommentsLeft(user.getCommentsLeft() + 1);
        user.setRank(checkRank(user.getCommentsLeft()));
        userRepository.save(user);
        log.info("CommentService - обновлена информация по пользователю: {}.", user);

        return CommentMapper.mapToCommentDto(comment);
    }

    // Получение всех комментариев пользователя.
    @Override
    public List<CommentDto> getAllCommentsByUserId(Long userId) {
        userService.getUserOrNotFound(userId);
        List<Comment> comments = commentRepository.getAllCommentsByUserId(userId);
        log.info("CommentService - предоставлены комментарии: {}.", comments);

        return CommentMapper.mapToCommentDto(comments);
    }

    // Редактирование комментария.
    @Override
    @Transactional
    public CommentDto updateComment(Long userId, Long commentId, CommentNewDto commentNewDto) {
        userService.getUserOrNotFound(userId);
        Comment comment = getCommentOrNotFound(commentId);
        comment.setText(commentNewDto.getText());
        commentRepository.save(comment);
        log.info("CommentService - изменён текст комментария: {}.", comment);

        return CommentMapper.mapToCommentDto(comment);
    }

    // Удаление комментария.
    @Override
    @Transactional
    public void userDeleteComment(Long userId, Long commentId) {
        userService.getUserOrNotFound(userId);
        Comment comment = getCommentOrNotFound(commentId);
        if (!comment.getUser().getId().equals(userId)) {
            throw new ValidatorExceptions("Нельзя удалить чужой комментарий!");
        }
        commentRepository.deleteById(commentId);
        log.info("CommentService - удалён комментарий c ИД: {}.", commentId);
    }

    // Административные сервисы, только для администраторов:
    // Удаление комментария.
    @Override
    @Transactional
    public void adminDeleteComment(Long commentId) {
        getCommentOrNotFound(commentId);
        commentRepository.deleteById(commentId);
        log.info("CommentService - удалён комментарий c ИД: {}.", commentId);
    }

    // Вспомогательные методы:

    // Получение комментария, если не найдена - ошибка 404.
    private Comment getCommentOrNotFound(Long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с ИД: " + commentId + ", не найден!"));
    }

    // Выставление рейтинга пользователя по количеству комментариев
    public Ranking checkRank(int commentsLeft) {
        if (commentsLeft < 3) {
            return Ranking.новичок;
        } else if (commentsLeft < 101) {
            return Ranking.участник;
        } else {
            return Ranking.ветеран;
        }
    }
}