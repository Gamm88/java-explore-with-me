package ru.practicum.event.model.event;

import lombok.*;
import ru.practicum.user.model.User;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.location.Location;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "events")
public class Event {
    // Идентификатор мероприятия.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Заголовок мероприятия.
    private String title;

    // Краткое описание мероприятия.
    private String annotation;

    // Категории к которой относится мероприятие.
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    // Нужно ли оплачивать участие в мероприятии (по умолчанию = false).
    private Boolean paid;

    // Дата и время на которые намечено мероприятие (в формате "yyyy-MM-dd HH:mm:ss").
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    // Пользователь, инициатор мероприятия.
    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;

    // Полное описание мероприятия.
    private String description;

    // Ограничение на количество участников, значение 0 или null - означает отсутствие ограничения.
    @Column(name = "participant_limit")
    private Integer participantLimit;

    // Дата и время публикации мероприятия (в формате "yyyy-MM-dd HH:mm:ss").
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    // Список состояний жизненного цикла мероприятия [PENDING, PUBLISHED, CANCELED].
    @Enumerated(EnumType.STRING)
    private State state;

    // Дата и время создания мероприятия (в формате "yyyy-MM-dd HH:mm:ss").
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    // Широта и долгота места проведения мероприятия.
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "lon"))
    })
    private Location location;

    // Нужна ли пре-модерация заявок на участие.
    // Если true, то все заявки будут ожидать подтверждения инициатором мероприятия.
    // Если false - то будут подтверждаться автоматически.
    @Column(name = "request_moderation")
    private Boolean requestModeration;

    // Количество одобренных заявок на участие в мероприятии.
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    // Количество просмотрев мероприятия.
    private Integer views;
}