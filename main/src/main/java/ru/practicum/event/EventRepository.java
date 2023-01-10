package ru.practicum.event;

import ru.practicum.event.model.event.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    // Получение мероприятий созданных пользователем
    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);
}