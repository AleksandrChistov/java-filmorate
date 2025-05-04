package ru.yandex.practicum.filmorate.dal.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.dal.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String INSERT_USER_QUERY = "INSERT users (name, login, email, birthday) VALUES (?, ?, ?, ?) returning id";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
    private static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";

    private final FriendshipDbStorage friendshipDbStorage;

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper, FriendshipDbStorage friendshipDbStorage) {
        super(jdbc, mapper);
        this.friendshipDbStorage = friendshipDbStorage;
    }

    @Override
    public User add(User newUser) {
        long id = insert(
                INSERT_USER_QUERY,
                newUser.getName(),
                newUser.getLogin(),
                newUser.getEmail(),
                newUser.getBirthday()
        );
        newUser.setId(id);
        return newUser;
    }

    @Override
    public User update(User updatedUser) {
        update(
                UPDATE_USER_QUERY,
                updatedUser.getName(),
                updatedUser.getLogin(),
                updatedUser.getEmail(),
                updatedUser.getBirthday(),
                updatedUser.getId()
        );
        return updatedUser;
    }

    @Override
    public boolean delete(long userId) {
        return delete(DELETE_USER_BY_ID_QUERY, userId);
    }

    @Override
    public List<User> getAll() {
        return findMany(FIND_ALL_USERS_QUERY);
    }

    @Override
    public Optional<User> getById(long userId) {
        return findOne(FIND_USER_BY_ID_QUERY, userId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        List<Long> commonFriendsIds = friendshipDbStorage.getCommonFriendsIds(userId, otherId);

        return commonFriendsIds.stream()
                .map(this::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public List<User> getFriends(long userId) {
        List<Long> friendsIds = friendshipDbStorage.getFriendsIds(userId);

        return friendsIds.stream()
                .map(this::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
