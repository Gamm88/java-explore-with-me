package ru.practicum.request;

import ru.practicum.request.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    // Получение всех запросов по ИД пользователя
    List<Request> findAllByRequesterId(Long userId);

    // Получение всех запросов по ИД события
    List<Request> findAllByEventId(Long eventId);
}
