package ru.practicum.stats.model.stats;

import lombok.*;

/**
 * Модель спецификации получения статистики, используется для получения статистики из БД, по множеству параметров
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsQueryParams {
    // Дата и время начала диапазона за который нужно выгрузить статистику (в формате "yyyy-MM-dd HH:mm:ss")
    private String start;

    // Дата и время конца диапазона за который нужно выгрузить статистику (в формате "yyyy-MM-dd HH:mm:ss")
    private String end;

    // Список uri для которых нужно выгрузить статистику
    private String[] uris;

    // Нужно ли учитывать только уникальные посещения (только с уникальным ip)
    private Boolean unique;
}