package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import ru.yandex.practicum.filmorate.dal.dto.GenreIdDto;
import ru.yandex.practicum.filmorate.dal.dto.MpaDto;
import ru.yandex.practicum.filmorate.validation.Group;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private int duration;
    @NotNull
    private MpaDto mpa;
    @Getter(AccessLevel.NONE)
    private final Set<GenreIdDto> genresIds = new HashSet<>();
    @Getter(AccessLevel.NONE)
    private final Set<Long> likesIds = new HashSet<>();

    public void addGenre(long genreId) {
        genresIds.add(new GenreIdDto(genreId));
    }

    public void addGenres(Set<GenreIdDto> newGenresIds) {
        genresIds.addAll(newGenresIds);
    }

    public void removeGenre(long genreId) {
        genresIds.remove(new GenreIdDto(genreId));
    }

    public Set<GenreIdDto> getGenres() {
        return new HashSet<>(genresIds);
    }

    public void addLike(long userId) {
        likesIds.add(userId);
    }

    public void addLikes(Set<Long> newLikes) {
        likesIds.addAll(newLikes);
    }

    public boolean removeLike(long userId) {
        return likesIds.remove(userId);
    }

    public Set<Long> getLikesIds() {
        return new HashSet<>(likesIds);
    }

    public int getLikesCount() {
        return likesIds.size();
    }
}
