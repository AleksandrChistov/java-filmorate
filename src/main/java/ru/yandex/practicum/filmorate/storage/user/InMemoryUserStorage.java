package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

import static ru.yandex.practicum.filmorate.util.CommonUtil.getNextId;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User add(User newUser) {
        newUser.setId(getNextId(users));
        log.debug("Пользователю был присвоен id = {}", newUser.getId());
        users.put(newUser.getId(), newUser);
        log.info("Пользователь успешно добавлен, id = {}", newUser.getId());
        return newUser;
    }

    @Override
    public User update(User newUser) {
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

    @Override
    public User delete(int userId) {
        return users.remove(userId);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        Set<Integer> userFriends = getFriendsIdsByUserId(userId);
        Set<Integer> friendFriends = getFriendsIdsByUserId(friendId);
        userFriends.add(friendId);
        friendFriends.add(userId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        Set<Integer> userFriends = getFriendsIdsByUserId(userId);
        Set<Integer> friendFriends = getFriendsIdsByUserId(friendId);
        userFriends.remove(friendId);
        friendFriends.remove(userId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        Set<Integer> userFriends = getFriendsIdsByUserId(userId);
        Set<Integer> otherFriends = getFriendsIdsByUserId(otherId);
        return userFriends.stream()
                .map(users::get)
                .filter(f -> f != null && otherFriends.contains(f.getId()))
                .toList();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(int id) {
        return users.get(id);
    }

    @Override
    public List<User> getFriends(int id) {
        return getFriendsIdsByUserId(id).stream()
                .map(users::get)
                .filter(Objects::nonNull)
                .toList();
    }

    private Set<Integer> getFriendsIdsByUserId(int userId) {
        User found = users.get(userId);
        if (found == null) {
            throw new NotFoundException("Пользователя с id = " + userId + " не найден.");
        }
        return found.getFriendsIds();
    }
}
