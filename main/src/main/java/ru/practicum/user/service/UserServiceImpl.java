package ru.practicum.user.service;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.user.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exeptions.NotFoundException;
import ru.practicum.exeptions.DuplicateException;
import ru.practicum.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    // Основные методы API:

    // Добавление нового пользователя.
    @Override
    public UserDto addUser(UserDto userDto) {
        checkUserNameUnique(userDto.getName());
        checkUserEmailUnique(userDto.getEmail());
        User createdUser = userRepository.save(UserMapper.mapToUser(userDto));
        log.info("UserService - добавлен новый пользователь: {}.", createdUser);

        return UserMapper.mapToUserDto(createdUser);
    }

    // Получение пользователей по списку ИД или всех.
    @Override
    public List<UserDto> getUsers(Long[] ids, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<UserDto> usersDtoList;

        if (ids == null) {
            usersDtoList = UserMapper.mapToUserDto(userRepository.findAll(pageRequest));
        } else {
            usersDtoList = UserMapper.mapToUserDto(userRepository.findAllById(Arrays.asList(ids)));
        }
        log.info("UserService - предоставлены пользователи: {}.", usersDtoList);

        return usersDtoList;
    }

    // Получение пользователя по ИД.
    @Override
    public UserDto getUser(Long userId) {
        User getUser = getUserOrNotFound(userId);
        UserDto userDto = UserMapper.mapToUserDto(getUser);
        log.info("UserService - предоставлен пользователь: {}.", userDto);

        return userDto;
    }

    // Редактирование пользователя.
    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = getUserOrNotFound(userId);
        String newName = userDto.getName();
        String newEmail = userDto.getEmail();

        if (newName != null) {
            checkUserNameUnique(newName);
            user.setName(newName);
        }
        if (newEmail != null) {
            checkUserEmailUnique(newEmail);
            user.setEmail(newEmail);
        }

        userRepository.save(user);
        log.info("UserService - обновлён пользователь: {}.", user);

        return UserMapper.mapToUserDto(user);
    }

    // Удаление пользователя.
    @Override
    public void deleteUser(Long userId) {
        getUserOrNotFound(userId);
        userRepository.deleteById(userId);
        log.info("UserService - удалён пользователь с ИД: {}", userId);
    }

    // Административные эндпоинты, только для администраторов сервиса:

    // Установка или снятие запрета на комментирование.
    @Override
    public void banOrUnbanCommentsForUser(Long userId, boolean ban) {
        User user = getUserOrNotFound(userId);

        if (ban) {
            user.setCommentsBan(true);
            userRepository.save(user);
            log.info("UserService - установлен запрет на комментарии для пользователя: {}", user);
        } else {
            user.setCommentsBan(false);
            userRepository.save(user);
            log.info("UserService - снят запрет на комментарии для пользователя: {}", user);
        }
    }

    // Вспомогательные методы:

    // Получение пользователя, если не найден - ошибка 404.
    @Override
    public User getUserOrNotFound(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ИД: " + userId + " не найден!"));
    }

    // Проверка на дублирование по имени пользователя.
    public void checkUserNameUnique(String userName) {
        if (userRepository.findByName(userName) != null) {
            throw new DuplicateException("Пользователь с именем - " + userName + ", уже существует!");
        }
    }

    // Проверка на дублирование по электронной почте пользователя.
    public void checkUserEmailUnique(String userEmail) {
        if (userRepository.findByEmail(userEmail) != null) {
            throw new DuplicateException("Пользователь с электронной почтой - " + userEmail + ", уже существует!");
        }
    }
}