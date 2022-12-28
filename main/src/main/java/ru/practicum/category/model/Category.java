package ru.practicum.category.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "categories")
public class Category {
    // Идентификатор категории (например: 3)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Название категории (например: Концерты)
    @Column(unique = true)
    private String name;
}