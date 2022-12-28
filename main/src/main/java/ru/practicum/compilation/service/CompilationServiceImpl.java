package ru.practicum.compilation.service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.compilation.model.*;
import ru.practicum.event.model.event.Event;
import org.springframework.stereotype.Service;
import ru.practicum.event.service.EventService;
import ru.practicum.exeptions.NotFoundException;
import org.springframework.data.domain.PageRequest;
import ru.practicum.compilation.CompilationRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventService eventService;
    private final CompilationMapper compilationMapper;
    private final CompilationRepository compilationRepository;

    /**
     * Публичные сервисы, для всех пользователей.
     */

    // Получение подборок.
    @Override
    public List<CompilationDto> getAllCompilations(boolean pinned, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Compilation> compilations = compilationRepository.findAllByPinnedIs(pinned, pageRequest);
        log.info("CompilationService - предоставлены подборки {}.", compilations);

        return compilationMapper.mapToCompilationDto(compilations);
    }

    // Получение подборки по его ИД.
    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = getCompilationOrNotFound(compId);
        log.info("CompilationService - предоставлена подборка {}.", compilation);

        return compilationMapper.mapToCompilationDto(compilation);
    }

    /**
     * Административные сервисы, только для администраторов.
     */

    // Добавление новой подборки.
    @Override
    public CompilationDto addCompilation(CompilationNewDto compilationNewDto) {
        Compilation compilation = compilationMapper.mapToCompilation(compilationNewDto);
        compilation.getEvents().replaceAll(event -> eventService.getEventOrNotFound(event.getId()));
        compilationRepository.save(compilation);

        log.info("CompilationService - добавлена подборка {}.", compilation);

        return compilationMapper.mapToCompilationDto(compilation);
    }

    // Удаление подборки.
    @Override
    public void deleteCompilation(Long compId) {
        getCompilationOrNotFound(compId);
        compilationRepository.deleteById(compId);
        log.info("CompilationService - удалена подборка c ИД: {}.", compId);
    }

    // Добавление события в подборку.
    @Override
    @Transactional
    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = getCompilationOrNotFound(compId);
        Event event = eventService.getEventOrNotFound(eventId);
        compilationRepository.addEventToCompilation(compId, eventId);
        log.info("CompilationService - добавлено событие: {}, в подборку: {}.", event, compilation);
    }

    // Удаление события из подборки.
    @Override
    @Transactional
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = getCompilationOrNotFound(compId);
        Event event = eventService.getEventOrNotFound(eventId);
        compilationRepository.deleteEventFromCompilation(compId, eventId);
        log.info("CompilationService - удалено событие: {}, из подборки: {}.", event, compilation);
    }

    // Закрепить или открепить подборку на главной странице.
    @Override
    @Transactional
    public void changePinned(Long compId, boolean pinned) {
        Compilation compilation = getCompilationOrNotFound(compId);
        compilationRepository.changePinned(compId, pinned);
        if (pinned) {
            log.info("CompilationService - закреплена подборки на главной странице: {}.", compilation);
        } else {
            log.info("CompilationService - откреплена подборки на главной странице: {}.", compilation);
        }
    }

    /**
     * Вспомогательные методы.
     */

    // Получение подборки, если не найдена - ошибка 404.
    private Compilation getCompilationOrNotFound(Long compId) {
        return compilationRepository
                .findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с ИД: " + compId + ", не найдена!"));
    }
}