package ru.practicum.event.model.location;

import lombok.*;

import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    // Широта места проведения события.
    private Float lat;

    // Долгота места проведения события.
    private Float lon;
}