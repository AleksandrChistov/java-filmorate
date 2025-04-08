package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();
    Film add(Film film);
    Film update(Film film);
    Film delete(int filmId);
    void addLike(int filmId, int userId);
    void deleteLike(int filmId, int userId);
    List<Film> getTop10Films();
}
