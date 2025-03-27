package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.Group;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.CommonUtil.getNextId;

@RestController
@RequestMapping(UserController.URL)
@Slf4j
public class UserController {
    public static final String URL = "/users";
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Validated(Group.OnCreate.class) @RequestBody User newUser) {
        log.info("Добавление нового пользователя");
        setLoginIfNameIsBlank(newUser);
        newUser.setId(getNextId(users));
        log.debug("Пользователю был присвоен id = {}", newUser.getId());
        users.put(newUser.getId(), newUser);
        log.info("Пользователь успешно добавлен, id = {}", newUser.getId());
        return newUser;
    }

    @PutMapping
    public User update(@Validated(Group.OnUpdate.class) @RequestBody User newUser) {
        log.info("Обновление пользователя с id = {}", newUser.getId());
        setLoginIfNameIsBlank(newUser);
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setName(newUser.getName());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Пользователь успешно обновлен, id = {}", newUser.getId());
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не был найден");
    }

    private static void setLoginIfNameIsBlank(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
            log.debug("Имя пользователя не заполнено, поэтому был присвоен логин вместо имени = {}", newUser.getName());
        }
    }

}
