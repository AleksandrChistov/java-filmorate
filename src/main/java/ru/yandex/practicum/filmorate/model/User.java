package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
public class User {
    public Long id;
    private String name;
    private String login;
    private String email;
    private LocalDate birthday;
    @Getter(AccessLevel.NONE)
    private final Set<Long> friendsIds = new HashSet<>();

    public void addFriend(long userId) {
        friendsIds.add(userId);
    }

    public void removeFriend(long userId) {
        friendsIds.remove(userId);
    }

    public Set<Long> getFriendsIdsCopy() {
        return new HashSet<>(friendsIds);
    }

    public Stream<Long> getFriendsIdsStream() {
        return friendsIds.stream();
    }
}
