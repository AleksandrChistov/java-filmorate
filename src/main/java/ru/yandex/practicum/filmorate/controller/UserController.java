package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepton.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.CommonUtil.getNextId;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        validate(newUser);
        newUser.setId(getNextId(users));
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        validate(newUser);
        if (newUser.getId() == null) {
            throw new ValidationException("id должно быть заполнено");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setName(newUser.getName());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setBirthday(newUser.getBirthday());
            return oldUser;
        }
        throw new ValidationException("Пользователь с id = " + newUser.getId() + " не был найден");
    }

    private void validate(User newUser) {
        if (newUser.getEmail() == null || !newUser.getEmail().contains("@")) {
            throw new ValidationException("Email должен содержать @");
        }
        if (newUser.getLogin() == null || newUser.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        if (newUser.getBirthday() == null || newUser.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть пустой или в будущем");
        }
    }

}
