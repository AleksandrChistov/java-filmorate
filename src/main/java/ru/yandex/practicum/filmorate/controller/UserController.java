package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.dto.NewUserDto;
import ru.yandex.practicum.filmorate.dal.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping(UserController.URL)
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    public static final String URL = "/users";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findAll() {
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto findById(@PathVariable @Min(1) long userId) {
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
    public void addFriend(@PathVariable @Min(1) long userId, @PathVariable @Min(1) long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteFriend(@PathVariable @Min(1) long userId, @PathVariable @Min(1) long friendId) {
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getFriends(@PathVariable @Min(1) long userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getCommonFriends(@PathVariable @Min(1) long userId, @PathVariable @Min(1) long otherId) {
        return userService.getCommonFriends(userId, otherId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean delete (@PathVariable @Min(1) long userId) {
        return userService.delete(userId);
    }

}
