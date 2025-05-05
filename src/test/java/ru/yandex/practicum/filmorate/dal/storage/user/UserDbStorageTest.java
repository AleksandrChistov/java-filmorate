package ru.yandex.practicum.filmorate.dal.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mapper.FriendshipRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.dal.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class, FriendshipDbStorage.class, FriendshipRowMapper.class})
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    void add() {
        User addedUser = new User(null, "Хомяк Байт", "bite", "bite@yandex.ru", LocalDate.of(2020, Month.JANUARY, 10));

        User added = userStorage.add(addedUser);

        assertThat(added).isNotNull().hasFieldOrPropertyWithValue("id", 6L);

        Optional<User> userOptional = userStorage.getById(6);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 6L)
                );
    }

    @Test
    void update() {
        String expectedNewEmail = "ivanov-new@yandex.ru";
        User updatedUser = new User(1L, "Иванов Иван", "ivanov", expectedNewEmail, LocalDate.of(2000, Month.FEBRUARY, 20));

        userStorage.update(updatedUser);

        Optional<User> userOptional = userStorage.getById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", expectedNewEmail)
                );
    }

    @Test
    void delete() {
        boolean deleted = userStorage.delete(2);

        assertThat(deleted).isTrue();

        Optional<User> userOptional = userStorage.getById(2);
        assertThat(userOptional).isEmpty();

        List<User> friends = userStorage.getFriends(2);
        assertThat(friends).isEmpty();
    }

    @Test
    void getAll() {
        List<User> users = userStorage.getAll();

        assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);
    }

    @Test
    void getById() {
        Optional<User> userOptional = userStorage.getById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    void getCommonFriends() {
        List<User> commonFriends = userStorage.getCommonFriends(1, 2);

        assertThat(commonFriends)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(commonFriends.getFirst())
                .hasFieldOrPropertyWithValue("id", 3L);
    }

    @Test
    void getFriends() {
        List<User> friends = userStorage.getFriends(1);

        assertThat(friends)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        assertThat(friends)
                .extracting("id")
                .containsExactlyInAnyOrder(3L, 5L);
    }
}