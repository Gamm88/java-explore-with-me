package ru.practicum.event.model.event;

import lombok.*;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.location.Location;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Событие, хранящееся в БД
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "events")
public class Event {
    // Идентификатор события (например: 3)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Заголовок события (например: Сплав на байдарках)
    private String title;

    // Краткое описание события (например: Сплав на байдарках похож на полет.)
    private String annotation;

    // id категории к которой относится событие (например: 2)
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    // Нужно ли оплачивать участие в событии (по умолчанию = false)
    private Boolean paid;

    // Дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    // Пользователь, краткая информация - id и name (например: 3, Фёдоров Матвей)
    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;

    // Полное описание события (например: Сплав на байдарках похож на полет. На спокойной воде — это парение.
    // На бурной, порожистой — выполнение фигур высшего пилотажа. И то, и другое дарят чувство обновления,
    // феерические эмоции, яркие впечатления.)
    private String description;

    // Ограничение на количество участников. Значение 0 или null - означает отсутствие ограничения
    @Column(name = "participant_limit")
    private Integer participantLimit;

    // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    // Список состояний жизненного цикла события [PENDING, PUBLISHED, CANCELED]
    @Enumerated(EnumType.STRING)
    private State state;

    // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    // Широта и долгота места проведения события
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "lon"))
    })
    private Location location;

    // Нужна ли пре-модерация заявок на участие.
    // Если true, то все заявки будут ожидать подтверждения инициатором события.
    // Если false - то будут подтверждаться автоматически.
    @Column(name = "request_moderation")
    private Boolean requestModeration;

    // Количество одобренных заявок на участие в событии (например: 5)
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    // Количество просмотрев события (например: 999)
    private Integer views;
}