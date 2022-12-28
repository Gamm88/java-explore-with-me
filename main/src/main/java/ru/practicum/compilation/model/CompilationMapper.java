package ru.practicum.compilation.model;

import ru.practicum.event.model.event.Event;
import org.springframework.stereotype.Component;
import ru.practicum.event.model.event.EventMapper;

import java.util.List;
import java.util.ArrayList;

@Component
public class CompilationMapper {
    private final EventMapper eventMapper = new EventMapper();

    // Из CompilationNewDto в Compilation.
    public Compilation mapToCompilation(CompilationNewDto compilationNewDto) {
        return Compilation.builder()
                .title(compilationNewDto.getTitle())
                .pinned(compilationNewDto.getPinned())
                .events(getEvents(compilationNewDto.getEvents()))
                .build();
    }

    // Из Compilation в CompilationDto.
    public CompilationDto mapToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(eventMapper.mapToEventShortDto(compilation.getEvents()))
                .build();
    }

    // Получение списка CompilationDto из списка Compilation.
    public List<CompilationDto> mapToCompilationDto(Iterable<Compilation> compilations) {
        List<CompilationDto> dtos = new ArrayList<>();
        for (Compilation compilation : compilations) {
            dtos.add(mapToCompilationDto(compilation));
        }
        return dtos;
    }

    // Создание списка событий из списка ИД событий.
    private List<Event> getEvents(List<Long> eventsId) {
        List<Event> events = new ArrayList<>();
        for (Long id : eventsId) {
            Event event = new Event();
            event.setId(id);
            events.add(event);
        }
        return events;
    }
}