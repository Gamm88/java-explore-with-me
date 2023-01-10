package ru.practicum.compilation.model;

import lombok.*;
import ru.practicum.event.model.event.Event;

import java.util.List;
import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "compilations")
public class Compilation {
    // Идентификатор подборки.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Заголовок подборки.
    @Column(unique = true)
    private String title;

    // Закреплена ли подборка на главной странице сайта.
    private Boolean pinned;

    // Мероприятия входящих в подборку (только уникальные).
    @ManyToMany
    @JoinTable(name = "compilations_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;
}