package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.dto.NewUserDto;
import ru.yandex.practicum.filmorate.dal.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.dal.dto.UserDto;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping(UserController.URL)
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final FilmService filmService;

    public static final String URL = "/users";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findAll() {
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto findById(@PathVariable @Positive Long userId) {
        return userService.getById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Validated @RequestBody NewUserDto newUser) {
        return userService.create(newUser);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@Validated @RequestBody UserDto newUser) {
        return userService.update(newUser);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteFriend(@PathVariable @Positive Long userId, @PathVariable @Positive Long friendId) {
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getFriends(@PathVariable @Positive Long userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getCommonFriends(@PathVariable @Positive Long userId, @PathVariable @Positive Long otherId) {
        return userService.getCommonFriends(userId, otherId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean delete(@PathVariable @Positive Long userId) {
        return userService.delete(userId);
    }

    @GetMapping("/{userId}/feed")
    @ResponseStatus(HttpStatus.OK)
    public List<Event> getEvent(@PathVariable @Positive Long userId) {
        return userService.getEvent(userId);
    }

    @GetMapping("/{userId}/recommendations")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseFilmDto> getRecommendations(@PathVariable @Positive Long userId) {
        return filmService.getRecommendationsForUser(userId);
    }
}
