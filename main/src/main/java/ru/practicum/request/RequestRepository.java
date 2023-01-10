package ru.practicum.request;

import ru.practicum.request.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    // Получение заявок пользователя на участие в мероприятиях.
    List<Request> findAllByRequesterId(Long userId);

    // Получение всех запросов на участие, по ИД мероприятия.
    List<Request> findAllByEventId(Long eventId);
}