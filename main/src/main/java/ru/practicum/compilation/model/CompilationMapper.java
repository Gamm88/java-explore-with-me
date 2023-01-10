package ru.practicum.compilation.model;

import ru.practicum.event.model.event.Event;
import ru.practicum.event.model.event.EventMapper;

import java.util.List;
import java.util.ArrayList;

public class CompilationMapper {
    private static final EventMapper eventMapper = new EventMapper();

    // Из CompilationNewDto в Compilation.
    public static Compilation mapToCompilation(CompilationNewDto compilationNewDto) {
        return Compilation.builder()
                .title(compilationNewDto.getTitle())
                .pinned(compilationNewDto.getPinned())
                .events(getEvents(compilationNewDto.getEvents()))
                .build();
    }

    // Из Compilation в CompilationDto.
    public static CompilationDto mapToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(eventMapper.mapToEventShortDto(compilation.getEvents()))
                .build();
    }

    // Получение списка CompilationDto из списка Compilation.
    public static List<CompilationDto> mapToCompilationDto(Iterable<Compilation> compilations) {
        List<CompilationDto> dtos = new ArrayList<>();
        for (Compilation compilation : compilations) {
            dtos.add(mapToCompilationDto(compilation));
        }
        return dtos;
    }

    // Создание списка мероприятий из списка ИД.
    private static List<Event> getEvents(List<Long> eventsId) {
        List<Event> events = new ArrayList<>();
        for (Long id : eventsId) {
            Event event = new Event();
            event.setId(id);
            events.add(event);
        }
        return events;
    }
}