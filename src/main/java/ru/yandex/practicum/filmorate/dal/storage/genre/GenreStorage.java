package ru.yandex.practicum.filmorate.dal.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {

    int addGenresByFilmId(Set<Long> genresIds, long filmId);

    List<Genre> getAll();

    Optional<Genre> getById(long genreId);

    List<Genre> getAllByFilmId(long filmId);
}
