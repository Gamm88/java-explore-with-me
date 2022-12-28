package ru.practicum.user;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.user.model.UserDto;
import ru.practicum.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("admin/users")
public class UserController {
    private final UserService userService;

    // создать пользователя
    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.info("UserController - создание пользователя: {}", userDto);

        return userService.addUser(userDto);
    }

    // получить пользователей по списку ИД или всех
    @GetMapping()
    public List<UserDto> getUsers(@RequestParam(value = "ids", required = false) Long[] ids,
                                  @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                  @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("UserController - получение списка пользователей");

        return userService.getUsers(ids, from, size);
    }

    // получить пользователя по ИД
    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable("userId") Long userId) {
        log.info("UserController - получение пользователя по ИД: {}", userId);

        return userService.getUser(userId);
    }


    // обновить пользователя по ИД
    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable("userId") Long userId,
                              @RequestBody UserDto userDto) {
        log.info("UserController - обновление пользователя с ИД: {}, новое значение: {}", userId, userDto);

        return userService.updateUser(userId, userDto);
    }

    // удалить пользователя по ИД
    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable("userId") Long userId) {
        log.info("UserController - удаление пользователя по ИД: {}", userId);
        userService.deleteById(userId);
    }
}