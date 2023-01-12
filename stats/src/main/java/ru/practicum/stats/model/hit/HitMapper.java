package ru.practicum.stats.model.hit;

import lombok.RequiredArgsConstructor;
import ru.practicum.components.DateUtility;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HitMapper {
    private final DateUtility dateUtility;

    // Из HitDto в Hit.
    public Hit mapToHit(HitDto hitDto) {
        return Hit.builder()
                .id(hitDto.getId())
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .hitDate(dateUtility.stringToDate(hitDto.getHitDate()))
                .build();
    }
}