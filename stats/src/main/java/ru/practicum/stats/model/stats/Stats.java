package ru.practicum.stats.model.stats;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
    private String app;
    private String uri;
    private int hits;
}
