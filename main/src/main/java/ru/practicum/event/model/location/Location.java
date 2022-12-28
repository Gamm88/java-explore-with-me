package ru.practicum.event.model.location;

import lombok.*;

import javax.persistence.Embeddable;

/**
 * Широта и долгота места проведения события
 */
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    // Широта (например: 55.754167)
    private Float lat;
    // Долгота (например: 37.62)
    private Float lon;
}
