package ru.yandex.practicum.filmorate.dal.storage.friendship;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

@Repository
public class FriendshipDbStorage extends BaseDbStorage<Friendship> implements FriendshipStorage {
    private static final String INSERT_FRIEND_QUERY = "INSERT friendship (user_id, friend_id) VALUES (?, ?) returning id";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_ALL_BY_USER_ID_QUERY = "SELECT * FROM friendship WHERE user_id = ?";

    public FriendshipDbStorage(JdbcTemplate jdbc, RowMapper<Friendship> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        insert(INSERT_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public boolean deleteFriend(long userId, long friendId) {
        return delete(DELETE_FRIEND_QUERY, userId, friendId);
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
        return findMany(FIND_ALL_BY_USER_ID_QUERY, userId)
                .stream()
                .map(Friendship::getFriendId)
                .toList();
    }
}
