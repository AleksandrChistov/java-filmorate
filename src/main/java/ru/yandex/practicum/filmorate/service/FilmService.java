package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dto.*;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreService genreService;
    private final FilmLikesService filmLikesService;
    private final MpaService mpaService;

    public FilmService(
            @Qualifier("filmDbStorage") FilmStorage filmStorage,
            GenreService genreService, FilmLikesService filmLikesService, MpaService mpaService
    ) {
        this.filmStorage = filmStorage;
        this.genreService = genreService;
        this.filmLikesService = filmLikesService;
        this.mpaService = mpaService;
    }

    public List<ResponseFilmDto> getAll() {
        return filmStorage.getAll().stream()
                .map(this::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public ResponseFilmDetailDto getById(long filmId) {
        return filmStorage.getById(filmId)
                .map(this::mapToFilmWithNamesDto)
                .orElseThrow(() -> new NotFoundException("Фильма с id = " + filmId + " не найден."));
    }

    public ResponseFilmDto create(NewFilmDto newFilm) {
        log.info("Добавление нового фильма с названием = {}", newFilm.getName());

        MpaDto mpaDto = mpaService.getById(newFilm.getMpa().getId());

        Film film = FilmMapper.mapNewToFilm(newFilm, mpaDto);
        film = filmStorage.add(film);

        log.info("Фильм успешно добавлен, id = {}", film.getId());

        if (newFilm.getGenres() != null) {
            Set<Long> genresIds = newFilm.getGenres().stream().map(GenreIdDto::getId).collect(Collectors.toSet());
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

    public List<ResponseFilmDto> getPopularFilmsByCount(long count) {
        log.info("Получение списка популярных фильмов в количестве {}", count);
        return filmStorage.getAll().stream()
                .map(this::mapToFilmDtoWithLikes)
                .sorted(Comparator.comparingInt((FilmWithLikes dto) -> dto.getLikes().size()).reversed())
                .map(FilmMapper::mapWithLikesToFilmDto)
                .limit(count)
                .collect(Collectors.toList());
    }

    private ResponseFilmDto mapToFilmDto(Film film) {
        List<GenreDto> genreDtos = genreService.getAllByFilmId(film.getId());
        return FilmMapper.mapToFilmDto(film, genreDtos);
    }

    private ResponseFilmDetailDto mapToFilmWithNamesDto(Film film) {
        List<GenreDto> genreDtos = genreService.getAllByFilmId(film.getId());
        return FilmMapper.mapToFilmWithNamesDto(film, genreDtos);
    }

    private FilmWithLikes mapToFilmDtoWithLikes(Film film) {
        List<GenreDto> genreDtos = genreService.getAllByFilmId(film.getId());
        Set<Long> likes = filmLikesService.getLikesByFilmId(film.getId());
        return FilmMapper.mapToFilmWithLikes(film, genreDtos, likes);
    }
}
