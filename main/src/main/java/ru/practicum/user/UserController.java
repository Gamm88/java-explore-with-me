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

    // Добавление нового пользователя.
    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.info("UserController - добавление нового пользователя: {}.", userDto);

        return userService.addUser(userDto);
    }

    // Получение пользователей по списку ИД или всех.
    @GetMapping()
    public List<UserDto> getUsers(@RequestParam(value = "ids", required = false) Long[] ids,
                                  @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                  @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("UserController - получение пользователей по списку ИД или всех.");

        return userService.getUsers(ids, from, size);
    }

    // Получение пользователя по ИД.
    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable("userId") Long userId) {
        log.info("UserController - получение пользователя по ИД: {}.", userId);

        return userService.getUser(userId);
    }

    // Редактирование пользователя.
    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable("userId") Long userId,
                              @RequestBody UserDto userDto) {
        log.info("UserController - обновление пользователя с ИД: {}, новое значение: {}.", userId, userDto);

        return userService.updateUser(userId, userDto);
    }

    // Удаление пользователя.
    @DeleteMapping("/{userId}")
    public String deleteById(@PathVariable("userId") Long userId) {
        log.info("UserController - удаление пользователя с ИД: {}.", userId);
        userService.deleteById(userId);

        return "Пользователь удалён.";
    }
}