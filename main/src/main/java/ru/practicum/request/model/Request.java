package ru.practicum.request.model;

import lombok.*;
import ru.practicum.user.model.User;
import ru.practicum.event.model.event.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Запрос на участие в событии, хранящийся в БД
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "requests")
public class Request {
    // Идентификатор запроса (например: 3)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Событие хранящееся в БД
    @ManyToOne()
    @JoinColumn(name = "event_id")
    private Event event;

    // Пользователь хранящийся в БД
    @ManyToOne()
    @JoinColumn(name = "requester_id")
    private User requester;

    // Дата и время создания заявки. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    private LocalDateTime created;

    // Статус заявки [PENDING, CONFIRMED, REJECTED, CANCELED]
    @Enumerated(EnumType.STRING)
    private Status status;
}