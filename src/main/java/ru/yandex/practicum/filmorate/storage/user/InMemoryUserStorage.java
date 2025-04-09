package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Stream;

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
    public void addFriend(User user, User friend) {
        log.info("Добавление в друзья друг другу пользователей {} и {}", user.getId(), friend.getId());
        user.addFiend(friend.getId());
        friend.addFiend(user.getId());
        log.info("Пользователи были успешно добавлены в друзья");
    }

    @Override
    public void deleteFriend(User user, User friend) {
        log.info("Удаление из друзей друг друга пользователей {} и {}", user.getId(), friend.getId());
        user.removeFriend(friend.getId());
        friend.removeFriend(user.getId());
        log.info("Пользователи были успешно удалены из друзей");
    }

    @Override
    public List<User> getCommonFriends(User user, User other) {
        log.info("Получение общих друзей пользователей {} и {}", user.getId(), other.getId());
        Stream<Integer> userFriendsStream = user.getFriendsIdsStream();
        Set<Integer> otherFriends = other.getFriendsIdsCopy();
        return userFriendsStream
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
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id = {} не найден.");
        }
        return users.get(id);
    }

    @Override
    public List<User> getFriends(User user) {
        log.info("Получение всех друзей пользователя {}", user.getId());
        return user.getFriendsIdsStream()
                .map(users::get)
                .filter(Objects::nonNull)
                .toList();
    }
}
