package ru.practicum.compilation;

import ru.practicum.compilation.model.Compilation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    // Получение подборок.
    List<Compilation> findAllByPinnedIs(boolean pinned, PageRequest pageRequest);

    // Добавление события в подборку.
    @Modifying
    @Query(value = "" +
            "INSERT INTO compilations_events (compilation_id, event_id) " +
            "VALUES (?1, ?2)", nativeQuery = true)
    void addEventToCompilation(Long compId, Long eventId);

    // Удаление события из подборки.
    @Modifying
    @Query(value = "" +
            "DELETE FROM compilations_events " +
            "WHERE compilation_id = ?1 AND event_id = ?2", nativeQuery = true)
    void deleteEventFromCompilation(Long compId, Long eventId);

    // Закрепить или открепить подборку на главной странице.
    @Modifying
    @Query(value = "" +
            "UPDATE compilations SET pinned = ?2 " +
            "WHERE id = ?1", nativeQuery = true)
    void changePinned(Long compId, boolean pinned);
}