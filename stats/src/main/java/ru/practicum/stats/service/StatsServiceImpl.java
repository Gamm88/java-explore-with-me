package ru.practicum.stats.service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.stats.model.hit.*;
import ru.practicum.stats.model.stats.*;
import ru.practicum.components.DateUtility;
import org.springframework.stereotype.Service;
import ru.practicum.stats.repository.StatsRepository;

import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final HitMapper hitMapper;
    private final DateUtility dateUtility;
    private final StatsRepository statsRepository;

    // Добавление информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
    @Override
    public void addHit(HitDto hitDto) {
        Hit hit = statsRepository.save(hitMapper.mapToHit(hitDto));
        log.info("StatsService - добавлен новый запроса к эндпоинту: {}.", hit);
    }

    // Получение статистики по посещениям эндпоинтов.
    @Override
    public List<Stats> getStats(StatsQueryParams statsQueryParams) {
        LocalDateTime start = dateUtility.stringToDate(statsQueryParams.getStart());
        LocalDateTime end = dateUtility.stringToDate(statsQueryParams.getEnd());
        List<String> uris = List.of(statsQueryParams.getUris());
        Boolean unique = statsQueryParams.getUnique();

        List<Hit> hits;

        if (uris.size() != 0 && !unique) {
            hits = statsRepository.findAllByHitDateBetweenAndUriIn(start, end, uris);
        } else if (uris.size() == 0 && unique) {
            hits = statsRepository.findAllByUniqueIp(start, end);
        } else if (uris.size() != 0) {
            hits = statsRepository.findAllByUrisAndUniqueIp(start, end, uris);
        } else {
            hits = statsRepository.findAllByHitDateBetween(start, end);
        }

        // Из списка запросов к эндпоинтам, создаём список статистики по эндпоинтам.
        List<Stats> stats = hits.stream()
                .map(hit -> new Stats(hit.getApp(), hit.getUri(),
                        // Добавляем количество просмотров (обращений к эндпоинту).
                        statsRepository.getViews(hit.getUri())))
                .collect(Collectors.toList());
        log.info("StatsService - предоставлена статистики по посещениям эндпоинтов: {}.", stats);

        return stats;
    }
}