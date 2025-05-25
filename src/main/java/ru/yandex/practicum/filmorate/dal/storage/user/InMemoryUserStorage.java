package ru.yandex.practicum.filmorate.dal.storage.user;

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
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User add(User newUser) {
        newUser.setId(getNextId(users));
        log.debug("Пользователю был присвоен id = {}", newUser.getId());
        users.put(newUser.getId(), newUser);
        log.info("Пользователь успешно добавлен, id = {}", newUser.getId());
        return newUser;
    }

    @Override
    public User update(User updatedUser) {
        if (users.containsKey(updatedUser.getId())) {
            User user = users.get(updatedUser.getId());
            user.setName(updatedUser.getName());
            user.setLogin(updatedUser.getLogin());
            user.setEmail(updatedUser.getEmail());
            user.setBirthday(updatedUser.getBirthday());
            log.info("Пользователь успешно обновлен, id = {}", updatedUser.getId());
            return user;
        }
        throw new NotFoundException("Пользователь с id = " + updatedUser.getId() + " не был найден");
    }

    @Override
    public boolean delete(long userId) {
        User deleted = users.remove(userId);
        return deleted != null;
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        log.info("Получение общих друзей пользователей {} и {}", userId, otherId);

        Stream<Long> userFriendsStream = findUser(userId).getFriendsIdsStream();
        Set<Long> otherFriends = findUser(otherId).getFriendsIdsCopy();

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
    public Optional<User> getById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> getFriends(long userId) {
        log.info("Получение всех друзей пользователя с ID = {}", userId);

        return findUser(userId).getFriendsIdsStream()
                .map(users::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void addFriend(long userId, long friendId) {
        Set<Long> userFriendsIds = findUser(userId).getFriendsIdsCopy();
        userFriendsIds.add(friendId);
    }

    @Override
    public boolean deleteFriend(long userId, long friendId) {
        Set<Long> userFriendsIds = findUser(userId).getFriendsIdsCopy();
        return userFriendsIds.remove(friendId);
    }

    @Override
    public List<Long> getCommonFriendsIds(long userId, long otherId) {
        List<Long> userFriendsIds = getFriendsIds(userId);
        List<Long> otherFriendsIds = getFriendsIds(otherId);

        return userFriendsIds.stream()
                .filter(otherFriendsIds::contains)
                .toList();
    }

    @Override
    public List<Long> getFriendsIds(long userId) {
        return findUser(userId).getFriendsIdsStream().toList();
    }

    private User findUser(long userId) {
        return getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = " + userId + " не найден."));
    }
}
