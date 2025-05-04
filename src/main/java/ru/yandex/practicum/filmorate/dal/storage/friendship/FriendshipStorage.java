package ru.yandex.practicum.filmorate.dal.storage.friendship;

import java.util.List;

public interface FriendshipStorage {
    void addFriend(long userId, long friendId);

    boolean deleteFriend(long userId, long friendId);

    List<Long> getFriendsIds(long userId);

    List<Long> getCommonFriendsIds(long userId, long friendId);
}
