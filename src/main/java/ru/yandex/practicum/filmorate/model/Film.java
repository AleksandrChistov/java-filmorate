package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import ru.yandex.practicum.filmorate.enums.RatingMPA;
import ru.yandex.practicum.filmorate.validation.Group;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@AllArgsConstructor
public class Film {
    @NotNull(groups = Group.OnUpdate.class)
    private Long id;
    @NotBlank(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    private String name;
    @NotBlank(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    @Size(max = 200, groups = {Group.OnCreate.class, Group.OnUpdate.class})
    private String description;
    @ReleaseDate(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    private LocalDate releaseDate;
    @NotNull(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    @Positive(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    private Integer duration;
    @NotNull
    private RatingMPA ratingMPA;
    @Getter(AccessLevel.NONE)
    private final Set<Long> likes = new HashSet<>();
    @Getter(AccessLevel.NONE)
    private final Set<String> genres = new HashSet<>();

    public void addLike(long userId) {
        likes.add(userId);
    }

    public void removeLike(long userId) {
        likes.remove(userId);
    }

    public int getLikesCount() {
        return likes.size();
    }

    public void addGenre(String genre) {
        genres.add(genre);
    }

    public void removeGenre(String genre) {
        genres.remove(genre);
    }

    public Set<String> getGenres() {
        return new HashSet<>(genres);
    }
}
