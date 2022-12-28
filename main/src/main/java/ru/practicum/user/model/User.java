package ru.practicum.user.model;

import lombok.*;

import javax.persistence.*;

/**
 * Модель пользователя хранящаяся в БД
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "users")
public class User {
    // Идентификатор пользователя (например: 3)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Имя (например: Фёдоров Матвей)
    private String name;

    // Электронная почта (например: petrov.i@practicummail.ru)
    @Column(unique = true)
    private String email;
}