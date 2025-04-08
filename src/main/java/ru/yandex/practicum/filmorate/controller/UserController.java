package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.Group;

import java.util.List;

@RestController
@RequestMapping(UserController.URL)
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    public static final String URL = "/users";

    @GetMapping
    public List<User> findAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable @Min(1) int id) {
        return userService.getById(id);
    }

    @PostMapping
    public User create(@Validated(Group.OnCreate.class) @RequestBody User newUser) {
        return userService.create(newUser);
    }

    @PutMapping
    public User update(@Validated(Group.OnUpdate.class) @RequestBody User newUser) {
        return userService.update(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable @Min(1) int id, @PathVariable @Min(1) int friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable @Min(1) int id, @PathVariable @Min(1) int friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable @Min(1) int id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getFriends(@PathVariable @Min(1) int id, @PathVariable @Min(1) int otherId) {
        return userService.getCommonFriends(id, otherId);
    }

}
