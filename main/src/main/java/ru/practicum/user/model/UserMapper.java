package ru.practicum.user.model;

import java.util.List;
import java.util.ArrayList;

public class UserMapper {
    //из UserDto в User
    public static User mapToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .rank(userDto.getRank())
                .commentsLeft(userDto.getCommentsLeft())
                .commentsBan(userDto.isCommentsBan())
                .build();
    }

    //из User в UserDto
    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .rank(user.getRank())
                .commentsLeft(user.getCommentsLeft())
                .commentsBan(user.isCommentsBan())
                .build();
    }

    //получение списка UserDto из списка User
    public static List<UserDto> mapToUserDto(Iterable<User> users) {
        List<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(mapToUserDto(user));
        }
        return dtos;
    }
}