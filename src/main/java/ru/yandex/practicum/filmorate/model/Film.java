package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import ru.yandex.practicum.filmorate.dal.dto.GenreDto;
import ru.yandex.practicum.filmorate.dal.dto.MpaDto;
import ru.yandex.practicum.filmorate.validation.Group;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
    private final Set<GenreDto> genres = new TreeSet<>(Comparator.comparing(GenreDto::getId));
    @Getter(AccessLevel.NONE)
    private final Set<Long> likesIds = new HashSet<>();

    public void addGenre(long genreId) {
        genres.add(new GenreDto(genreId));
    }

    public void addGenres(Set<GenreDto> newGenresIds) {
        genres.addAll(newGenresIds);
    }

    public void removeGenre(long genreId) {
        genres.removeIf(g -> g.getId() == genreId);
    }

    public Set<GenreDto> getGenres() {
        Set<GenreDto> genreDtos = new TreeSet<>(Comparator.comparing(GenreDto::getId));
        genreDtos.addAll(genres);
        return genreDtos;
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
