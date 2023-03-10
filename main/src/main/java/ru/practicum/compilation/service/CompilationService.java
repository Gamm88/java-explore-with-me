package ru.practicum.compilation.service;

import ru.practicum.compilation.model.CompilationDto;
import ru.practicum.compilation.model.CompilationNewDto;

import java.util.List;

public interface CompilationService {
    // Публичные методы API, для всех пользователей:

    // Получение подборок.
    List<CompilationDto> getCompilations(boolean pinned, int from, int size);

    // Получение подборки по ИД.
    CompilationDto getCompilationById(Long compId);

    // Административные методы API, только для администраторов:

    // Добавление новой подборки.
    CompilationDto addCompilation(CompilationNewDto compilationNewDto);

    // Удаление подборки.
    void deleteCompilation(Long compId);

    // Добавление мероприятия в подборку.
    void addEventToCompilation(Long compId, Long eventId);

    // Удаление мероприятия из подборки.
    void deleteEventFromCompilation(Long compId, Long eventId);

    // Закрепить или открепить подборку на главной странице.
    void changePinned(Long compId, boolean pinned);
}