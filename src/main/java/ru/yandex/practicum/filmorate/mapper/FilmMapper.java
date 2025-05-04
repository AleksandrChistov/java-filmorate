package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

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

    public static FilmDto mapToFilmDto(Film film) {
        return new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaId(),
                film.getGenresIds(),
                film.getLikesIds()
        );
    }
}
