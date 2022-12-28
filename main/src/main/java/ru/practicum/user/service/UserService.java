package ru.practicum.user.service;

import ru.practicum.user.model.User;
import ru.practicum.user.model.UserDto;

import java.util.List;

public interface UserService {
    // создать пользователя
    UserDto addUser(UserDto userDto);

    // получить пользователей по списку ИД или всех
    List<UserDto> getUsers(Long[] ids, int from, int size);

    // получить пользователя по ИД
    UserDto getUser(Long userId);

    // обновление пользователя
    UserDto updateUser(Long userId, UserDto userDto);

    // удалить пользователя по ИД
    void deleteById(Long userId);

    // получение пользователя, если не найден - ошибка 404
    User getUserOrNotFound(Long userId);
}