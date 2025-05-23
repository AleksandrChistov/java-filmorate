package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.dal.dto.MpaDto;
import ru.yandex.practicum.filmorate.dal.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.model.Film;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {

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

        if (filmDto.getDirectors() != null) {
            film.addDirectors(filmDto.getDirectors());
        }

        return film;
    }

    public static ResponseFilmDto mapToFilmDto(Film film) {
        return new ResponseFilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa(),
                film.getGenres(),
                film.getDirectors(),
                film.getLikesIds()
        );
    }
}
