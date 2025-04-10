package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film getById(int id);

    Film add(Film film);

    Film update(Film film);

    Film delete(int filmId);

    void addLike(Film film, int userId);

    void deleteLike(Film film, int userId);

    List<Film> getPopularFilmsByCount(int count);
}
