package ru.practicum.user.service;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.exeptions.DuplicateException;
import ru.practicum.user.model.User;
import lombok.RequiredArgsConstructor;
import ru.practicum.user.model.UserDto;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.UserMapper;
import org.springframework.stereotype.Service;
import ru.practicum.exeptions.NotFoundException;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    // создать пользователя
    @Override
    public UserDto addUser(UserDto userDto) {
        checkCategoryNameUnique(userDto.getName());
        User createdUser = userRepository.save(UserMapper.mapToUser(userDto));
        log.info("UserService - в базу добавлен пользователь: {} ", createdUser);

        return UserMapper.mapToUserDto(createdUser);
    }

    // получить пользователей по списку ИД или всех
    @Override
    public List<UserDto> getUsers(Long[] ids, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<UserDto> usersDtoList;

        if (ids != null) {
            usersDtoList = UserMapper.mapToUserDto(userRepository.findAllById(Arrays.asList(ids)));
        } else {
            usersDtoList = UserMapper.mapToUserDto(userRepository.findAll(pageRequest));
        }

        log.info("UserService - предоставлен список пользователей: {} ", usersDtoList);

        return usersDtoList;
    }

    // получить пользователя по ИД
    @Override
    public UserDto getUser(Long userId) {
        User getUser = getUserOrNotFound(userId);
        UserDto userDto = UserMapper.mapToUserDto(getUser);
        log.info("UserService - по ИД: {} получен пользователь: {}", userId, userDto);

        return userDto;
    }

    // обновление пользователя
    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User updatedUser = getUserOrNotFound(userId);

        String newEmail = userDto.getEmail();
        String newName = userDto.getName();
        checkCategoryNameUnique(newName);

        if (newEmail != null) {
            updatedUser.setEmail(newEmail);
        }
        if (newName != null) {
            updatedUser.setName(newName);
        }

        updatedUser = userRepository.save(updatedUser);
        log.info("UserService - в базе обновлён пользователь: {}", updatedUser);

        return UserMapper.mapToUserDto(updatedUser);
    }

    // удалить пользователя по ИД
    @Override
    public void deleteById(Long userId) {
        getUserOrNotFound(userId);
        log.info("UserController - удаление пользователя по ИД: {}", userId);
        userRepository.deleteById(userId);
    }

    // получение пользователя, если не найден - ошибка 404
    @Override
    public User getUserOrNotFound(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ИД " + userId + " не найден."));
    }

    // Проверка на дублирование по имени пользователя.
    public void checkCategoryNameUnique(String userName) {
        if (userRepository.findByName(userName) != null) {
            throw new DuplicateException("Пользователь с именем - " + userName + ", уже существует!");
        }
    }
}