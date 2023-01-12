package ru.practicum.request.model;

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
@Table(name = "requests")
public class Request {
    // Идентификатор запроса.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Мероприятие, на которое осуществляется запрос.
    @ManyToOne()
    @JoinColumn(name = "event_id")
    private Event event;

    // Пользователь отправивший запрос.
    @ManyToOne()
    @JoinColumn(name = "requester_id")
    private User requester;

    // Дата и время создания заявки, в формате "yyyy-MM-dd HH:mm:ss"
    private LocalDateTime created;

    // Статус запроса [PENDING, CONFIRMED, REJECTED, CANCELED].
    @Enumerated(EnumType.STRING)
    private Status status;
}