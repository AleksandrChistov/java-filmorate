package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.dal.dto.GenreDto;
import ru.yandex.practicum.filmorate.dal.dto.MpaDto;
import ru.yandex.practicum.filmorate.dal.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreService genreService;
    private final MpaService mpaService;

    public FilmService(
            @Qualifier("filmDbStorage") FilmStorage filmStorage,
            GenreService genreService, MpaService mpaService
    ) {
        this.filmStorage = filmStorage;
        this.genreService = genreService;
        this.mpaService = mpaService;
    }

    public List<ResponseFilmDto> getAll() {
        return mapToFilmsDtos(filmStorage.getAll());
    }

    public ResponseFilmDto getById(long filmId) {
        return filmStorage.getById(filmId)
                .map(this::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Фильма с id = " + filmId + " не найден."));
    }

    public ResponseFilmDto create(FilmDto newFilm) {
        log.info("Добавление нового фильма с названием = {}", newFilm.getName());

        MpaDto mpaDto = mpaService.getById(newFilm.getMpa().getId());

        Film film = FilmMapper.mapToFilm(newFilm, mpaDto);
        film = filmStorage.add(film);

        log.info("Фильм успешно добавлен, id = {}", film.getId());

        if (newFilm.getGenres() != null) {
            Set<Long> genresIds = newFilm.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet());
            genreService.addGenresByFilmId(genresIds, film.getId());
        }

        return mapToFilmDto(film);
    }

    public ResponseFilmDto update(FilmDto newFilm) {
        log.info("Обновление фильма с id = {}", newFilm.getId());

        MpaDto mpaDto = mpaService.getById(newFilm.getMpa().getId());

        Film updatedFilm = filmStorage.getById(newFilm.getId())
                .map(u -> FilmMapper.mapToFilm(newFilm, mpaDto))
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден."));

        updatedFilm.setId(newFilm.getId());

        updatedFilm = filmStorage.update(updatedFilm);

        log.info("Фильм успешно обновлен, id = {}", updatedFilm.getId());

        return mapToFilmDto(updatedFilm);
    }

    public boolean delete(long filmId) {
        return filmStorage.delete(filmId);
    }

    public List<ResponseFilmDto> getPopularFilmsByCount(int count) {
        log.info("Получение списка популярных фильмов в количестве {}", count);
        List<Film> films = filmStorage.getPopularFilmsByCount(count);
        return mapToFilmsDtos(films);
    }

    public void addLike(long filmId, long userId) {
        log.info("Добавление лайка пользователем {} к фильму {}", userId, filmId);
        filmStorage.addLike(filmId, userId);
        log.info("Лайк успешно добавлен");
    }

    public boolean deleteLike(long filmId, long userId) {
        log.info("Удаление лайка пользователем {} из фильма {}", userId, userId);
        boolean isDeleted = filmStorage.deleteLike(filmId, userId);
        if (isDeleted) {
            log.info("Лайк успешно удален");
        }
        return isDeleted;
    }

    private ResponseFilmDto mapToFilmDto(Film film) {

        Set<GenreDto> genreDtos = genreService.getAllByFilmId(film.getId());
        Set<Long> likes = new HashSet<>(filmStorage.getLikesByFilmId(film.getId()));

        film.addGenres(genreDtos);
        film.addLikes(likes);

        return FilmMapper.mapToFilmDto(film);
    }

    private List<ResponseFilmDto> mapToFilmsDtos(List<Film> films) {
        List<Long> filmsIds = films.stream().map(Film::getId).toList();

        Map<Long, Set<Genre>> filmIdToGenresMap = genreService.getAllByFilmIds(filmsIds);
        Map<Long, List<Long>> filmIdToLikesMap = filmStorage.getLikesByFilmIds(filmsIds);

        return films.stream().map(film -> {
            Set<Genre> genres = filmIdToGenresMap.getOrDefault(film.getId(), new HashSet<>());
            Set<GenreDto> genreDtos = genres.stream().map(GenreMapper::mapToGenreDto).collect(Collectors.toSet());

            Set<Long> likes = new HashSet<>(filmIdToLikesMap.getOrDefault(film.getId(), new ArrayList<>()));

            film.addGenres(genreDtos);
            film.addLikes(likes);

            return FilmMapper.mapToFilmDto(film);
        }).collect(Collectors.toList());
    }
}
