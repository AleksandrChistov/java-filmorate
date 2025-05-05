package ru.yandex.practicum.filmorate.dal.storage.friendship;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mapper.FriendshipRowMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FriendshipDbStorage.class, FriendshipRowMapper.class})
class FriendshipDbStorageTest {
    private final FriendshipDbStorage friendshipStorage;

    @Test
    void addFriend() {
        friendshipStorage.addFriend(3, 2);

        List<Long> friendshipIds = friendshipStorage.getFriendsIds(3);

        assertThat(friendshipIds).containsExactlyInAnyOrder(2L);
    }

    @Test
    void deleteFriend() {
        boolean deleted = friendshipStorage.deleteFriend(1, 5);

        assertThat(deleted).isTrue();

        List<Long> friendshipIds = friendshipStorage.getFriendsIds(1);

        assertThat(friendshipIds).containsExactlyInAnyOrder(3L);
    }

    @Test
    void getCommonFriendsIds() {
        List<Long> commonFriendsIds = friendshipStorage.getCommonFriendsIds(1, 2);

        assertThat(commonFriendsIds).containsExactlyInAnyOrder(3L);
    }

    @Test
    void getFriendsIds() {
        List<Long> friendsIds = friendshipStorage.getFriendsIds(1);

        assertThat(friendsIds).containsExactlyInAnyOrder(3L, 5L);
    }
}