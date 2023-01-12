package ru.practicum.comment.model;

import lombok.*;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentNewDto {
    // Текст комментария.
    @NotBlank(message = "Комментарий не может быть пустым!")
    @Size(message = "Комментарий не может быть больше 4000 символов)", max = 4000)
    private String text;
}