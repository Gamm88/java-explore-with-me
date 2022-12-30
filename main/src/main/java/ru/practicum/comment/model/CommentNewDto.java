package ru.practicum.comment.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentNewDto {
    // Текст комментария.
    @NotBlank
    private String text;
}