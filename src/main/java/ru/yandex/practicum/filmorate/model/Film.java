package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.dal.dto.DirectorDto;
import ru.yandex.practicum.filmorate.dal.dto.GenreDto;
import ru.yandex.practicum.filmorate.dal.dto.MpaDto;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@AllArgsConstructor
public class Film {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private int duration;
    @NotNull
    private MpaDto mpa;
    @Getter(AccessLevel.NONE)
    private final Set<GenreDto> genres = new TreeSet<>(Comparator.comparing(GenreDto::getId));
    private final Set<DirectorDto> directors = new TreeSet<>(Comparator.comparing(DirectorDto::getId));
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

    public void addDirectors(Set<DirectorDto> newDirectrosIds) {
        directors.addAll(newDirectrosIds);
    }

    public Set<DirectorDto> getDirectors() {
        Set<DirectorDto> directorDtos = new TreeSet<>(Comparator.comparing(DirectorDto::getId));
        directorDtos.addAll(directors);
        return directorDtos;
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
