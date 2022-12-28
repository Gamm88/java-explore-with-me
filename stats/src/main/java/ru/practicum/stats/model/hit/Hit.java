package ru.practicum.stats.model.hit;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "hits")
public class Hit {
    // Идентификатор записи.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Идентификатор сервиса для которого записывается информация.
    @Column
    private String app;

    // URI для которого был осуществлен запрос.
    @Column
    private String uri;

    // IP-адрес пользователя, осуществившего запрос.
    @Column
    private String ip;

    // Дата и время, когда был совершен запрос к эндпоинту.
    @Column(name = "hit_date")
    private LocalDateTime hitDate;
}