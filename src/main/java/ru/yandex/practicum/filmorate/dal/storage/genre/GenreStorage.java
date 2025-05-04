package ru.yandex.practicum.filmorate.dal.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    List<Genre> getAll();

    Optional<Genre> getById(long genreId);

    List<Genre> getAllByFilmId(long filmId);
}
