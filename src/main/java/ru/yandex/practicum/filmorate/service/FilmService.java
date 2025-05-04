package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<FilmDto> getAll() {
        // todo: get Genre and likesIds from its DBs
        return filmStorage.getAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto getById(long filmId) {
        return filmStorage.getById(filmId)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Фильма с id = " + filmId + " не найден."));
    }

    public FilmDto create(FilmDto newFilm) {
        log.info("Добавление нового фильма с названием = {}", newFilm.getName());

        Film film = FilmMapper.mapToFilm(newFilm);
        film = filmStorage.add(film);

        log.info("Фильм успешно добавлен, id = {}", film.getId());

        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto update(long filmId, FilmDto newFilm) {
        log.info("Обновление фильма с id = {}", filmId);

        Film updatedFilm = filmStorage.getById(filmId)
                .map(u -> FilmMapper.mapToFilm(newFilm))
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден."));

        updatedFilm.setId(filmId);

        updatedFilm = filmStorage.update(updatedFilm);

        log.info("Фильм успешно обновлен, id = {}", updatedFilm.getId());

        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public boolean delete(long filmId) {
        return filmStorage.delete(filmId);
    }

    public void addLike(long filmId, long userId) {
        log.info("Добавление лайка пользователем {} к фильму {}", userId, filmId);
        checkUserIsPresent(userId);
        filmStorage.addLike(filmId, userId);
        log.info("Лайк успешно добавлен");
    }

    public boolean deleteLike(long filmId, long userId) {
        log.info("Удаление лайка пользователем {} из фильма {}", userId, userId);
        checkUserIsPresent(userId);
        boolean isDeleted = filmStorage.deleteLike(filmId, userId);
        if (isDeleted) {
            log.info("Лайк успешно удален");
        }
        return isDeleted;
    }

    public List<FilmDto> getPopularFilmsByCount(long count) {
        log.info("Получение списка популярных фильмов в количестве {}", count);
        return filmStorage.getPopularFilmsByCount(count).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    private void checkUserIsPresent(long userId) {
        userService.getById(userId);
    }
}
