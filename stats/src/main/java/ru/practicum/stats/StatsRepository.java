package ru.practicum.stats;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.model.hit.Hit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {

    List<Hit> findAllByHitDateBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "" +
            "SELECT DISTINCT ip " +
            "FROM hits " +
            "WHERE hit_date BETWEEN ?1 AND ?2 ",
            nativeQuery = true)
    List<Hit> findAllByUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(value = "" +
            "SELECT DISTINCT ip " +
            "FROM hits " +
            "WHERE hit_date BETWEEN ?1 AND ?2 AND uri IN(?3)",
            nativeQuery = true)
    List<Hit> findAllByUrisAndUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<Hit> findAllByHitDateBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = "" +
            "SELECT COUNT(ip) " +
            "FROM hits " +
            "WHERE uri LIKE ?1",
            nativeQuery = true)
    int getViews(String uri);
}