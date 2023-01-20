package ru.practicum.comment.model;

import ru.practicum.user.model.User;
import ru.practicum.event.model.event.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class CommentMapper {
    // Из CommentNewDto в Comment.
    public static Comment mapToComment(User user, Event event, CommentNewDto commentNewDto) {
        return Comment.builder()
                .text(commentNewDto.getText())
                .user(user)
                .event(event)
                .createdOn(LocalDateTime.now())
                .build();
    }

    // Из Comment в CommentDto.
    public static CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .userName(comment.getUser().getName())
                .eventTitle(comment.getEvent().getTitle())
                .text(comment.getText())
                .createdOn(comment.getCreatedOn())
                .build();
    }

    // Получение списка CommentDto из списка Comment.
    public static List<CommentDto> mapToCommentDto(Iterable<Comment> comments) {
        List<CommentDto> dtos = new ArrayList<>();
        for (Comment comment : comments) {
            dtos.add(mapToCommentDto(comment));
        }
        return dtos;
    }
}