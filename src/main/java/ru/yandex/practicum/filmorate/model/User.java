package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import ru.yandex.practicum.filmorate.validation.Group;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
public class User {
    @NotNull(groups = Group.OnUpdate.class)
    public Integer id;
    private String name;
    @NotBlank(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    private String login;
    @NotBlank(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    @Email(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    private String email;
    @NotNull(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    @Past(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    private LocalDate birthday;
    @Getter(AccessLevel.NONE)
    private final Set<Integer> friendsIds = new HashSet<>();

    public void addFiend(int userId) {
        friendsIds.add(userId);
    }

    public void removeFriend(int userId) {
        friendsIds.remove(userId);
    }

    public Set<Integer> getFriendsIdsCopy() {
        return new HashSet<>(friendsIds);
    }

    public Stream<Integer> getFriendsIdsStream() {
        return friendsIds.stream();
    }
}
