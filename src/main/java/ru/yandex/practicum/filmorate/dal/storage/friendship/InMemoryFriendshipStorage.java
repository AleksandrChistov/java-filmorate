package ru.yandex.practicum.filmorate.dal.storage.friendship;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFriendshipStorage implements FriendshipStorage {
    private final Map<Long, List<Long>> friendship = new HashMap<>();

    @Override
    public void addFriend(long userId, long friendId) {
        List<Long> userFriendsIds = friendship.getOrDefault(userId, new ArrayList<>());
        userFriendsIds.add(friendId);
        friendship.put(userId, userFriendsIds);
    }

    @Override
    public boolean deleteFriend(long userId, long friendId) {
        List<Long> friendsIds = friendship.getOrDefault(userId, new ArrayList<>());
        return friendsIds.remove(friendId);
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
        return friendship.getOrDefault(userId, new ArrayList<>());
    }
}
