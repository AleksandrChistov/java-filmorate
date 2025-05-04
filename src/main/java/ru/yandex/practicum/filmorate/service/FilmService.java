package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(long id) {
        return findFilm(id);
    }

    public Film create(Film newFilm) {
        return filmStorage.add(newFilm);
    }

    public Film update(long filmId, Film newFilm) {
        newFilm.setId(filmId);
        return filmStorage.update(newFilm);
    }

    public boolean delete(long filmId) {
        return filmStorage.delete(filmId);
    }

    public void addLike(long filmId, long userId) {
        checkUserIsPresent(userId);
        Film film = findFilm(filmId);
        filmStorage.addLike(film, userId);
    }

    public void deleteLike(long filmId, long userId) {
        checkUserIsPresent(userId);
        Film film = findFilm(filmId);
        filmStorage.deleteLike(film, userId);
    }

    public List<Film> getPopularFilmsByCount(long count) {
        return filmStorage.getPopularFilmsByCount(count);
    }

    private Film findFilm(long filmId) {
        return filmStorage.getById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильма с id = " + filmId + " не найден."));
    }

    private void checkUserIsPresent(long userId) {
        userService.getById(userId);
    }
}
