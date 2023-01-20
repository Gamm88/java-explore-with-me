package ru.practicum.comment.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentDto {
    // Имя пользователь, который оставил комментарий.
    private String userName;

    // Заголовок события, для которого оставил комментарий.
    private String eventTitle;

    // Текст комментария.
    private String text;

    // Дата и время создания комментария (в формате "yyyy-MM-dd HH:mm:ss").
    private LocalDateTime createdOn;
}