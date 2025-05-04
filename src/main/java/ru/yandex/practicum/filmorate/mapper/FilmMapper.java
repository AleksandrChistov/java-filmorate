package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.dal.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {

    public static Film mapToFilm(FilmDto filmDto) {
        Film film = new Film(
                null,
                filmDto.getName(),
                filmDto.getDescription(),
                filmDto.getReleaseDate(),
                filmDto.getDuration(),
                filmDto.getMpaId()
        );

        film.addGenres(filmDto.getGenresIds());
        film.addLikes(filmDto.getLikesIds());

        return film;
    }

    public static FilmDto mapToFilmDto(Film film, List<GenreDto> genreDto, Set<Long> filmLikes) {
        Set<Long> genresIds = genreDto.stream().map(GenreDto::getId).collect(Collectors.toSet());

        return new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaId(),
                genresIds,
                filmLikes
        );
    }
}
