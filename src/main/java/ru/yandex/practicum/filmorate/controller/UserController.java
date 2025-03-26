package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.excepton.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

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
    public User create(@Valid @RequestBody User newUser) {
        try {
            log.info("Добавление нового пользователя");
            log.debug("Пользователь прошёл валидацию: {}", newUser);
            setLoginIfNameIsBlank(newUser);
            newUser.setId(getNextId(users));
            log.debug("Пользователю был присвоен id = {}", newUser.getId());
            users.put(newUser.getId(), newUser);
            log.info("Пользователь успешно добавлен, id = {}", newUser.getId());
            return newUser;
        } catch (ValidationException e) {
            log.warn("Ошибка валидации при добавлении пользователя: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при добавлении пользователя: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при добавлении пользователя");
        }
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        try {
            log.info("Обновление пользователя с id = {}", newUser.getId());
            if (newUser.getId() == null) {
                throw new ValidationException("id должно быть заполнено");
            }
            log.debug("Пользователь для обновления прошёл валидацию: {}", newUser);
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
        } catch (ValidationException e) {
            log.warn("Ошибка валидации при обновлении пользователя: {}", e.getMessage());
            throw e;
        } catch (NotFoundException e) {
            log.warn("Ошибка при обновлении пользователя: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при обновлении пользователя: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при обновлении пользователя");
        }
    }

    private static void setLoginIfNameIsBlank(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
            log.debug("Имя пользователя не заполнено, поэтому был присвоен логин вместо имени = {}", newUser.getName());
        }
    }

}
