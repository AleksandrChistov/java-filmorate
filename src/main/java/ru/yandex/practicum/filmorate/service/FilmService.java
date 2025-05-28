package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dto.*;
import ru.yandex.practicum.filmorate.dal.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Event;
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
    private final DirectorService directorService;
    private final EventStorage eventStorage;
    private final UserService userService;

    public FilmService(
            FilmStorage filmStorage, GenreService genreService, MpaService mpaService,
            EventStorage eventStorage, DirectorService directorService, UserService userService) {
        this.filmStorage = filmStorage;
        this.genreService = genreService;
        this.mpaService = mpaService;
        this.eventStorage = eventStorage;
        this.directorService = directorService;
        this.userService = userService;
    }

    public List<ResponseFilmDto> getAll() {
        return mapToFilmsDtos(filmStorage.getAll());
    }

    public List<ResponseFilmDto> getCommonFilms(long userId, long friendId) {
        log.info("Получение общих любимых фильмов пользователей {} и {}", userId, friendId);

        List<Film> commonFilms = filmStorage.getCommonFilms(userId, friendId);

        return mapToFilmsDtos(commonFilms);
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

        if (!newFilm.getGenres().isEmpty()) {
            Set<Long> genresIds = newFilm.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet());
            genreService.addGenresByFilmId(genresIds, film.getId());
        }

        if (!newFilm.getDirectors().isEmpty()) {
            directorService.addDirectorsByFilmId(film);
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

        genreService.deleteGenresByFilmId(updatedFilm.getId());
        if (!newFilm.getGenres().isEmpty()) {
            Set<Long> genresIds = newFilm.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet());
            genreService.addGenresByFilmId(genresIds, updatedFilm.getId());
        }


        directorService.removeByFilmId(updatedFilm.getId());

        if (!newFilm.getDirectors().isEmpty()) {
            directorService.addDirectorsByFilmId(updatedFilm);
        }

        log.info("Фильм успешно обновлен, id = {}", updatedFilm.getId());

        return mapToFilmDto(updatedFilm);
    }

    public boolean delete(long filmId) {
        return filmStorage.delete(filmId);
    }

    public void addLike(long filmId, long userId) {
        log.info("Добавление лайка пользователем {} к фильму {}", userId, filmId);
        filmStorage.addLike(filmId, userId);
        eventStorage.add(new Event(
                System.currentTimeMillis(),
                userId,
                EventType.LIKE,
                Operation.ADD,
                null,
                filmId
        ));
        log.info("Лайк успешно добавлен");
    }

    public boolean deleteLike(long filmId, long userId) {
        log.info("Удаление лайка пользователем {} из фильма {}", userId, userId);

        userService.getById(userId);

        boolean isDeleted = filmStorage.deleteLike(filmId, userId);

        if (isDeleted) {
            log.info("Лайк успешно удален");
        }

        eventStorage.add(new Event(
                System.currentTimeMillis(),
                userId,
                EventType.LIKE,
                Operation.REMOVE,
                null,
                filmId
        ));

        return isDeleted;
    }

    public List<ResponseFilmDto> findPopularFilms(int count, Long genreId, Long year) {
        if (genreId != null && year != null) {
            log.info("Получение списка популярных фильмов {} года в жанре {}, в количестве {}", year, genreId, count);
            List<Film> films = filmStorage.findPopularFilmsByGenreByYear(count, genreId, year);
            return mapToFilmsDtos(films);
        } else if (genreId != null) {
            log.info("Получение списка популярных фильмов в жанре {}, в количестве {}", genreId, count);
            List<Film> films = filmStorage.findPopularFilmsByGenre(count, genreId);
            return mapToFilmsDtos(films);
        } else if (year != null) {
            log.info("Получение списка популярных фильмов {} года, в количестве {}", year, count);
            List<Film> films = filmStorage.findPopularFilmsByYear(count, year);
            return mapToFilmsDtos(films);
        } else {
            log.info("Получение списка популярных фильмов в количестве {}", count);
            List<Film> films = filmStorage.getPopularFilmsByCount(count);
            return mapToFilmsDtos(films);
        }
    }

    public List<ResponseFilmDto> getFilmsDirectorById(Long directorId, String sortBy) {
        directorService.getById(directorId);

        if (sortBy.equalsIgnoreCase("likes")) {
            log.info("Получение списка фильмов режиссера с id = {}, отсортированным по количеству лайков", directorId);
            List<Film> films = filmStorage.getFilmsDirectorLikes(directorId);
            return mapToFilmsDtos(films);
        } else if (sortBy.equalsIgnoreCase("year")) {
            log.info("Получение списка фильмов режиссера с id = {}, отсортированным по году выпуска", directorId);
            List<Film> films = filmStorage.getFilmsDirectorYear(directorId);
            return mapToFilmsDtos(films);
        } else {
            log.warn("Некорректное значение параметра sortBy = {}, возможные значения: Likes, Year", sortBy);
            throw new NotFoundException(String.format(
                    "Некорректное значение параметра sortBy = %s, возможные значения: Likes, Year", sortBy));
        }
    }

    public List<ResponseFilmDto> getFilmsFromSearch(String query, String by) {
        if (by.equalsIgnoreCase("title")) {
            List<Film> films = filmStorage.getFilmsSearchTitle(query.toLowerCase());
            return mapToFilmsDtos(films);
        } else if (by.equalsIgnoreCase("director")) {
            List<Film> films = filmStorage.getFilmsSearchDirector(query.toLowerCase());
            return mapToFilmsDtos(films);
        } else if (by.equalsIgnoreCase("title,director") || by.equalsIgnoreCase("director,title")) {
            List<Film> films = filmStorage.getFilmsSearchTitle(query.toLowerCase());
            films.addAll(filmStorage.getFilmsSearchDirector(query.toLowerCase()));
            return mapToFilmsDtos(films).stream()
                    .sorted(Comparator.comparing((ResponseFilmDto f) -> f.getLikes().size()).reversed())
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    public List<ResponseFilmDto> getRecommendationsForUser(long userId) {
        List<Film> films = filmStorage.getRecommendationsForUser(userId);
        return mapToFilmsDtos(films);
    }

    public ResponseFilmDto mapToFilmDto(Film film) {

        Set<GenreDto> genreDtos = genreService.getAllByFilmId(film.getId());
        Set<Long> likes = new HashSet<>(filmStorage.getLikesByFilmId(film.getId()));
        Set<DirectorDto> directorDtos = directorService.getAllByFilmId(film.getId());

        film.addGenres(genreDtos);
        film.addLikes(likes);
        film.addDirectors(directorDtos);

        return FilmMapper.mapToFilmDto(film);
    }

    public List<ResponseFilmDto> mapToFilmsDtos(List<Film> films) {
        List<Long> filmsIds = films.stream().map(Film::getId).toList();

        Map<Long, Set<Genre>> filmIdToGenresMap = genreService.getAllByFilmIds(filmsIds);
        Map<Long, List<Long>> filmIdToLikesMap = filmStorage.getLikesByFilmIds(filmsIds);
        Map<Long, Set<Director>> filmIdToDirectorsMap = directorService.getAllByFilmIds(filmsIds);

        return films.stream().map(film -> {
            Set<Genre> genres = filmIdToGenresMap.getOrDefault(film.getId(), new HashSet<>());
            Set<GenreDto> genreDtos = genres.stream().map(GenreMapper::mapToGenreDto).collect(Collectors.toSet());

            Set<Long> likes = new HashSet<>(filmIdToLikesMap.getOrDefault(film.getId(), new ArrayList<>()));

            Set<Director> directors = filmIdToDirectorsMap.getOrDefault(film.getId(), new HashSet<>());
            Set<DirectorDto> directorDtos = directors.stream().map(DirectorMapper::mapToDirectorDto).collect(Collectors.toSet());

            film.addGenres(genreDtos);
            film.addLikes(likes);
            film.addDirectors(directorDtos);

            return FilmMapper.mapToFilmDto(film);
        }).collect(Collectors.toList());
    }
}
