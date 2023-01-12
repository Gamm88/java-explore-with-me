package ru.practicum.stats.service;

import ru.practicum.stats.model.stats.*;
import ru.practicum.stats.model.hit.HitDto;

import java.util.List;

public interface StatsService {
    // Добавление информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
    void addHit(HitDto hitDto);

    // Получение статистики по посещениям эндпоинтов.
    List<Stats> getStats(StatsQueryParams statsQueryParams);
}