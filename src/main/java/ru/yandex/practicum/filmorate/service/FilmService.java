package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(int id) {
        return filmStorage.getById(id);
    }

    public Film create(Film newFilm) {
        return filmStorage.add(newFilm);
    }

    public Film update(Film newFilm) {
        return filmStorage.update(newFilm);
    }

    public Film delete(int filmId) {
        return filmStorage.delete(filmId);
    }

    public void addLike(int filmId, int userId) {
        userStorage.getById(userId);
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        userStorage.getById(userId);
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilmsByCount(int count) {
        return filmStorage.getPopularFilmsByCount(count);
    }
}
