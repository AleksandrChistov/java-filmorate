package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dal.dto.UserDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User mapToUser(UserDto userDto) {
        return new User(
                null,
                userDto.getName(),
                userDto.getLogin(),
                userDto.getEmail(),
                userDto.getBirthday()
        );
    }

    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday()
        );
    }
}
