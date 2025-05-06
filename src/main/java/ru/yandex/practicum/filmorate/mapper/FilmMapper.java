package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dal.dto.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {

    public static Film mapNewToFilm(NewFilmDto filmDto, MpaDto mpaDto) {
        Film film = new Film(
                null,
                filmDto.getName(),
                filmDto.getDescription(),
                filmDto.getReleaseDate(),
                filmDto.getDuration(),
                mpaDto
        );

        if (filmDto.getGenres() != null) {
            film.addGenres(filmDto.getGenres());
        }

        return film;
    }

    public static Film mapToFilm(FilmDto filmDto, MpaDto mpaDto) {
        Film film = new Film(
                filmDto.getId(),
                filmDto.getName(),
                filmDto.getDescription(),
                filmDto.getReleaseDate(),
                filmDto.getDuration(),
                mpaDto
        );

        if (filmDto.getGenres() != null) {
            film.addGenres(filmDto.getGenres());
        }

        return film;
    }

    public static ResponseFilmDto mapToFilmDto(Film film, List<GenreDto> genreDtos) {
        List<GenreIdDto> genresIdDtos = genreDtos.stream().map(GenreMapper::mapToGenreId).collect(Collectors.toList());

        return new ResponseFilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                new MpaIdDto(film.getMpa().getId()),
                genresIdDtos
        );
    }

    public static ResponseFilmDetailDto mapToFilmWithNamesDto(Film film, List<GenreDto> genreDtos) {
        return new ResponseFilmDetailDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa(),
                genreDtos
        );
    }

    public static FilmWithLikes mapToFilmWithLikes(Film film, List<GenreDto> genreDto, Set<Long> likes) {
        Set<GenreIdDto> genresIds = genreDto.stream().map(GenreMapper::mapToGenreId).collect(Collectors.toSet());

        return new FilmWithLikes(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                new MpaIdDto(film.getMpa().getId()),
                genresIds,
                likes
        );
    }

    public static ResponseFilmDto mapWithLikesToFilmDto(FilmWithLikes film) {
        return new ResponseFilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa(),
                new ArrayList<>(film.getGenres())
        );
    }
}
