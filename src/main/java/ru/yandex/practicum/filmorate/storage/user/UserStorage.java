package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    List<User> getFriends(User friend);

    User getById(int id);

    User add(User user);

    User update(User user);

    User delete(int userId);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    List<User> getCommonFriends(User user, User other);
}
