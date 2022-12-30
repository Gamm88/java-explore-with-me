package ru.practicum.comment.model;

import ru.practicum.user.model.User;
import ru.practicum.event.model.event.Event;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;

@Component
public class CommentMapper {
    // Из CommentNewDto в Comment.
    public Comment mapToComment(User user, Event event, CommentNewDto commentNewDto) {
        return Comment.builder()
                .text(commentNewDto.getText())
                .user(user)
                .event(event)
                .build();
    }

    // Из Comment в CommentDto.
    public CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .userName(comment.getUser().getName())
                .eventTitle(comment.getEvent().getTitle())
                .text(comment.getText())
                .build();
    }

    // Получение списка CommentDto из списка Comment.
    public List<CommentDto> mapToCommentDto(Iterable<Comment> comments) {
        List<CommentDto> dtos = new ArrayList<>();
        for (Comment comment : comments) {
            dtos.add(mapToCommentDto(comment));
        }
        return dtos;
    }
}