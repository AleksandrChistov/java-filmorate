package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    List<User> getFriends(int id);

    User getById(int id);

    User add(User user);

    User update(User user);

    User delete(int userId);

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<User> getCommonFriends(int userId, int otherId);
}
