package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.Group;

import java.util.Collection;

@RestController
@RequestMapping(UserController.URL)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    public static final String URL = "/users";

    @GetMapping
    public Collection<User> findAll() {
        return userService.getAll();
    }

    @PostMapping
    public User create(@Validated(Group.OnCreate.class) @RequestBody User newUser) {
        return userService.create(newUser);
    }

    @PutMapping
    public User update(@Validated(Group.OnUpdate.class) @RequestBody User newUser) {
        return userService.update(newUser);
    }

}
