package ru.practicum.comment;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.comment.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.service.CommentService;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * Приватные эндпоинты, только для пользователей прошедших авторизацию.
     */

    // Добавление нового комментария.
    @PostMapping("/users/{userId}/comments/events/{eventId}")
    public CommentDto addComment(@Positive @PathVariable(value = "userId") Long userId,
                                 @Positive @PathVariable(value = "eventId") Long eventId,
                                 @RequestBody @Valid CommentNewDto commentNewDto) {
        log.info("CommentService - добавление нового комментария: {}.", commentNewDto);

        return commentService.addComment(userId, eventId, commentNewDto);
    }

    // Получение комментария по ИД.
    @GetMapping("comments/{commentId}")
    public CommentDto getComment(@PathVariable Long commentId) {
        log.info("CommentService - получение комментария с ИД: {}.", commentId);

        return commentService.getComment(commentId);
    }

    // Удаление комментария.
    @DeleteMapping("/users/{userId}/comments/{commentId}")
    public String deleteComment(@PathVariable Long userId,
                                @PathVariable Long commentId) {
        log.info("CommentService - удаление комментария с ИД: {}.", commentId);
        commentService.deleteComment(userId, commentId);

        return "Комментарий удалён.";
    }

    /**
     * Административные эндпоинты, только для администраторов.
     */

    // Получение всех комментариев пользователя.
    @GetMapping("/admin/comments/users/{userId}")
    public List<CommentDto> getAllCommentsByUserId(@PathVariable Long userId) {
        log.info("CommentService - получение всех комментариев пользователя с ИД: {}.", userId);

        return commentService.getAllCommentsByUserId(userId);
    }

    // Получение всех комментариев события.
    @GetMapping("/admin/comments/events/{eventId}")
    public List<CommentDto> getAllCommentsByEventId(@PathVariable Long eventId) {
        log.info("CommentService - получение всех комментариев события с ИД: {}.", eventId);

        return commentService.getAllCommentsByEventId(eventId);
    }

    // Редактирование комментария.
    @PatchMapping("/admin/comments/{userId}/{commentId}")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @RequestBody @Valid CommentNewDto commentNewDto) {
        log.info("CommentService - редактирование комментария с ИД: {}, новый текст : {}. ", commentId, commentNewDto);

        return commentService.updateComment(userId, commentId, commentNewDto);
    }
}