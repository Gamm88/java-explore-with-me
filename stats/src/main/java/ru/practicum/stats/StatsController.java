package ru.practicum.stats;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.stats.model.stats.*;
import ru.practicum.stats.model.hit.HitDto;
import ru.practicum.stats.service.StatsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    // Добавление информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
    // Название сервиса, uri и ip пользователя указаны в теле запроса.
    @PostMapping("/hit")
    public String addHit(@RequestBody HitDto hitDto) {
        log.info("HitController - сохранение информации о запросе к эндпоинту: {}.", hitDto);
        statsService.addHit(hitDto);

        return "Информация сохранена.";
    }

    // Получение статистики по посещениям эндпоинтов.
    // Параметры запроса связываем с экземпляром класса - StatsQueryParams.
    @GetMapping("/stats")
    public List<Stats> getStats(StatsQueryParams statsQueryParams) {
        log.info("HitController - получение статистики по параметрам: {}.", statsQueryParams);

        return statsService.getStats(statsQueryParams);
    }
}