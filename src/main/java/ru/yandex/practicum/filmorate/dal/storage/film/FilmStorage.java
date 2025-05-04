package ru.yandex.practicum.filmorate.dal.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dal.storage.Storage;

import java.util.List;

public interface FilmStorage extends Storage<Film> {

    void addLike(long filmId, long userId);

    boolean deleteLike(long filmId, long userId);

    List<Film> getPopularFilmsByCount(long count);
}
