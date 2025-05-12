package ru.yandex.practicum.filmorate.dal.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dal.storage.Storage;

import java.util.List;

public interface UserStorage extends Storage<User> {
    List<User> getFriends(long userId);

    List<User> getCommonFriends(long userId, long friendId);

    void addFriend(long userId, long friendId);

    boolean deleteFriend(long userId, long friendId);

    List<Long> getFriendsIds(long userId);

    List<Long> getCommonFriendsIds(long userId, long friendId);
}
