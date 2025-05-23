package ru.yandex.practicum.filmorate.dal.storage.film;

import ru.yandex.practicum.filmorate.dal.storage.Storage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage extends Storage<Film> {
    List<Film> getPopularFilmsByCount(int count);

    List<Long> getLikesByFilmId(long filmId);

    Map<Long, List<Long>> getLikesByFilmIds(List<Long> filmIds);

    void addLike(long filmId, long userId);

    boolean deleteLike(long filmId, long userId);
}
