package ru.practicum.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.model.CompilationDto;
import ru.practicum.compilation.model.CompilationNewDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    // Публичные эндпоинты, для всех пользователей:

    // Получение всех подборок.
    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(value = "pinned", required = false) boolean pinned,
                                                @PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                                int from,
                                                @Positive @RequestParam(value = "size", defaultValue = "10")
                                                int size) {
        log.info("CompilationController - получение всех подборок.");

        return compilationService.getCompilations(pinned, from, size);
    }

    // Получение подборки по ИД.
    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("CompilationController - получение подборки с ИД: {}.", compId);

        return compilationService.getCompilationById(compId);
    }

    // Административные эндпоинты, только для администраторов:

    // Добавление новой подборки.
    @PostMapping("/admin/compilations")
    public CompilationDto addCompilation(@Valid @RequestBody CompilationNewDto compilationNewDto) {
        log.info("CompilationController - добавление новой подборки: {}.", compilationNewDto);

        return compilationService.addCompilation(compilationNewDto);
    }

    // Удаление подборки.
    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseBody
    public String deleteCompilation(@PathVariable("compId") Long compId) {
        log.info("CompilationController - удаление подборки с ИД: {}.", compId);
        compilationService.deleteCompilation(compId);

        return "Подборка удалена.";
    }

    // Добавление мероприятия в подборку.
    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    @ResponseBody
    public String addEventToCompilation(@PathVariable("compId") Long compId,
                                        @PathVariable("eventId") Long eventId) {
        log.info("CompilationController - добавление мероприятия с ИД: {}, в подборку с ИД: {}.", eventId, compId);
        compilationService.addEventToCompilation(compId, eventId);

        return "Мероприятие добавлено в подборку.";
    }

    // Удаление мероприятия из подборки.
    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    @ResponseBody
    public String deleteEventFromCompilation(@PathVariable("compId") Long compId,
                                             @PathVariable("eventId") Long eventId) {
        log.info("CompilationController - удаление мероприятия с ИД: {}, из подборки с ИД: {}.", eventId, compId);
        compilationService.deleteEventFromCompilation(compId, eventId);

        return "Мероприятие удалено из подборки.";
    }

    // Закрепить подборку на главной странице.
    @PatchMapping("/admin/compilations/{compId}/pin")
    @ResponseBody
    public String pinCompilationToMainPage(@PathVariable("compId") Long compId) {
        log.info("CompilationController - закрепление подборки с ИД: {} на главной странице.", compId);
        compilationService.changePinned(compId, true);

        return "Подборка закреплена на главной странице.";
    }

    // Открепить подборку на главной странице.
    @DeleteMapping("/admin/compilations/{compId}/pin")
    @ResponseBody
    public String unpinCompilationToMainPage(@PathVariable("compId") Long compId) {
        log.info("CompilationController - открепление подборки с ИД: {} на главной странице.", compId);
        compilationService.changePinned(compId, false);

        return "Подборка откреплена с главной страницы.";
    }
}