package ru.yandex.practicum.filmorate.dal.storage.filmLikes;

import ru.yandex.practicum.filmorate.model.FilmLikes;

import java.util.List;

public interface FilmLikesStorage {

    void addLike(long filmId, long userId);

    boolean deleteLike(long filmId, long userId);

    List<FilmLikes> getLikesByFilmId(long filmId);
}
