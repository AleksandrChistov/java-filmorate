package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(int id) {
        return userStorage.getById(id);
    }

    public List<User> getFriends(int id) {
        User user = findUser(id);
        return userStorage.getFriends(user);
    }

    public User create(User newUser) {
        log.info("Добавление нового пользователя");
        setLoginIfNameIsBlank(newUser);
        return userStorage.add(newUser);
    }

    public User update(User newUser) {
        log.info("Обновление пользователя с id = {}", newUser.getId());
        setLoginIfNameIsBlank(newUser);
        return userStorage.update(newUser);
    }

    public User delete(int userId) {
        return userStorage.delete(userId);
    }

    public void addFriend(int userId, int friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);
        userStorage.addFriend(user, friend);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);
        userStorage.deleteFriend(user, friend);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        User user = findUser(userId);
        User other = findUser(otherId);
        return userStorage.getCommonFriends(user, other);
    }

    private static void setLoginIfNameIsBlank(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
            log.debug("Имя пользователя не заполнено, поэтому был присвоен логин вместо имени = {}", newUser.getName());
        }
    }

    private User findUser(int userId) {
        User found = userStorage.getById(userId);
        if (found == null) {
            throw new NotFoundException("Пользователя с id = " + userId + " не найден.");
        }
        return found;
    }
}
