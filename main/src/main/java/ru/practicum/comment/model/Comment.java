package ru.practicum.comment.model;

import lombok.*;
import ru.practicum.user.model.User;
import ru.practicum.event.model.event.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "comments")
public class Comment {
    // Идентификатор комментария.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Текст комментария.
    @Column
    private String text;

    // Пользователь, который оставил комментарий.
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Мероприятие, для которого оставил комментарий.
    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    // Дата и время создания комментария (в формате "yyyy-MM-dd HH:mm:ss").
    @Column(name = "created_on")
    private LocalDateTime createdOn;
}