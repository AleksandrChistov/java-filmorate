package ru.yandex.practicum.filmorate.dal.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String INSERT_USER_QUERY = "INSERT INTO users (name, login, email, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
    private static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";

    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_ALL_BY_USER_ID_QUERY = "SELECT friend_id FROM friendship WHERE user_id = ?";
    private static final String GET_RECOMMENDATIONS_QUERY = "SELECT f.* " +
            "FROM films AS f " +
            "JOIN films_likes AS fl ON f.id = fl.film_id " +
            "JOIN (" +
                "SELECT fl2.user_id AS ui " +
                "FROM films_likes AS fl1 " +
                "JOIN films_likes AS fl2 ON fl1.film_id = fl2.film_id " +
                "WHERE fl1.user_id = ? AND fl2.user_id != ? " +
                "GROUP BY fl2.user_id " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 1" +
            ") " +
            "AS similar_users ON fl.user_id = similar_users.user_id " +
            "WHERE f.id NOT IN (SELECT film_id FROM films_likes WHERE user_id = ?" +
            ")";

    private final RowMapper<User> userMapper;
    private final RowMapper<Film> filmMapper;

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> userMapper, RowMapper<Film> filmMapper) {
        super(jdbc);
        this.userMapper = userMapper;
        this.filmMapper = filmMapper;
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
        return findMany(FIND_ALL_USERS_QUERY, userMapper);
    }

    @Override
    public Optional<User> getById(long userId) {
        return findOne(FIND_USER_BY_ID_QUERY, userMapper, userId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        List<Long> commonFriendsIds = getCommonFriendsIds(userId, otherId);

        return commonFriendsIds.stream()
                .map(this::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public List<User> getFriends(long userId) {
        List<Long> friendsIds = getFriendsIds(userId);

        return friendsIds.stream()
                .map(this::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
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
        return jdbc.queryForList(FIND_ALL_BY_USER_ID_QUERY, Long.class, userId);
    }

    public List<Film> getRecommendations(long userId) {
        return findManyFilms(GET_RECOMMENDATIONS_QUERY, filmMapper, userId, userId, userId);
    }
}
