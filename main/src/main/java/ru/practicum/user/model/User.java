package ru.practicum.user.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "users")
public class User {
    // Идентификатор пользователя.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Имя пользователя.
    @Column(unique = true)
    private String name;

    // Электронная почта пользователя.
    @Column(unique = true)
    private String email;

    // Рейтинг пользователя (по кол-ву комментариев).
    private Ranking rank;

    // Количество оставленных комментариев.
    @Column(name = "comments_left")
    private Integer commentsLeft;

    // Бан на комментарии.
    @Column(name = "comments_ban")
    private boolean commentsBan;
}