package ru.practicum.user.service;

import ru.practicum.user.model.*;

import java.util.List;

public interface UserService {

    /**
     * Основные методы API.
     */

    // Добавление нового пользователя.
    UserDto addUser(UserDto userDto);

    // Получение пользователей по списку ИД или всех.
    List<UserDto> getUsers(Long[] ids, int from, int size);

    // Получение пользователя по ИД.
    UserDto getUser(Long userId);

    // Редактирование пользователя.
    UserDto updateUser(Long userId, UserDto userDto);

    // Удаление пользователя.
    void deleteUser(Long userId);

    /**
     * Вспомогательные методы.
     */

    // Получение пользователя, если не найден - ошибка 404.
    User getUserOrNotFound(Long userId);
}