package ru.yandex.practicum.filmorate.service;

import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dto.NewUserDto;
import ru.yandex.practicum.filmorate.dal.dto.UserDto;
import ru.yandex.practicum.filmorate.dal.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.dal.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dal.dto.ResponseFilmDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final EventStorage eventStorage;
    private final FilmService filmService;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, EventStorage eventStorage, FilmService filmService) {
        this.userStorage = userStorage;
        this.eventStorage = eventStorage;
        this.filmService = filmService;

    }

    public List<UserDto> getAll() {
        return userStorage.getAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getById(long id) {
        return findUser(id);
    }

    public List<UserDto> getFriends(long userId) {
        log.info("Получение всех друзей пользователя {}", userId);

        findUser(userId);

        return userStorage.getFriends(userId).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto create(NewUserDto newUser) {
        log.info("Добавление нового пользователя");

        User user = UserMapper.mapNewToUser(newUser);

        setLoginIfNameIsBlank(user);

        user = userStorage.add(user);

        log.info("Пользователь успешно добавлен, id = {}", user.getId());

        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UserDto newUser) {
        log.info("Обновление пользователя с id = {}", newUser.getId());

        User updatedUser = userStorage.getById(newUser.getId())
                .map(u -> UserMapper.mapToUser(newUser))
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден."));

        setLoginIfNameIsBlank(updatedUser);

        updatedUser.setId(newUser.getId());

        updatedUser = userStorage.update(updatedUser);

        log.info("Пользователь успешно обновлен, id = {}", updatedUser.getId());

        return UserMapper.mapToUserDto(updatedUser);
    }

    public boolean delete(long userId) {
        return userStorage.delete(userId);
    }

    public void addFriend(long userId, long friendId) {
        log.info("Добавление в друзья пользователя с ID = {}", friendId);
        findUser(userId);
        findUser(friendId);
        userStorage.addFriend(userId, friendId);
        eventStorage.addEvent(new Event(
                System.currentTimeMillis(),
                userId,
                EventType.FRIEND,
                Operation.ADD,
                null,
                friendId
        ));
        log.info("Пользователь с ID = {} был добавлен в друзья", friendId);
    }

    public boolean deleteFriend(long userId, long friendId) {
        log.info("Удаление из друзей пользователя с ID {}", friendId);

        findUser(userId);
        findUser(friendId);

        boolean isDeleted = userStorage.deleteFriend(userId, friendId);
        eventStorage.addEvent(new Event(
                System.currentTimeMillis(),
                userId,
                EventType.FRIEND,
                Operation.REMOVE,
                null,
                friendId
        ));
        if (isDeleted) {
            log.info("Пользователь с ID = {} был удален из друзей", friendId);
        }
        return isDeleted;
    }

    public List<UserDto> getCommonFriends(long userId, long otherId) {
        log.info("Получение общих друзей пользователей {} и {}", userId, otherId);

        return userStorage.getCommonFriends(userId, otherId).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    private static void setLoginIfNameIsBlank(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
            log.debug("Имя пользователя не заполнено, поэтому был присвоен логин вместо имени = {}", newUser.getName());
        }
    }

    private UserDto findUser(long userId) {
        return userStorage.getById(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден."));
    }

    public List<Event> getEvent(@Min(1) long userId) {
        userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден."));
        return eventStorage.getFeedByUserId(userId);
    }

    public List<ResponseFilmDto> getRecommendations(long userId) {
        List<Film> films = userStorage.getRecommendations(userId);
        return filmService.mapToFilmsDtos(films);
    }
}
