package ru.practicum.comment.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    // Имя пользователь, который оставил комментарий.
    private String userName;

    // Заголовок события, для которого оставил комментарий.
    private String eventTitle;

    // Текст комментария.
    private String text;
}