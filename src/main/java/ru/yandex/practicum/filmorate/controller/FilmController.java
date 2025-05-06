package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.dal.dto.NewFilmDto;
import ru.yandex.practicum.filmorate.dal.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.dal.dto.ResponseFilmDetailDto;
import ru.yandex.practicum.filmorate.service.FilmLikesService;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping(FilmController.URL)
@RequiredArgsConstructor
@Validated
public class FilmController {
    private final FilmService filmService;
    private final FilmLikesService filmLikesService;

    public static final String URL = "/films";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseFilmDto> findAll() {
        return filmService.getAll();
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseFilmDetailDto findById(@PathVariable @Min(1) long filmId) {
        return filmService.getById(filmId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseFilmDto> findPopularFilmsByCount(@RequestParam(defaultValue = "10") long count) {
        return filmService.getPopularFilmsByCount(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseFilmDto create(@Validated @RequestBody NewFilmDto newFilm) {
        return filmService.create(newFilm);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseFilmDto update(@Validated @RequestBody FilmDto newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable @Min(1) long filmId, @PathVariable @Min(1) long userId) {
        filmLikesService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteLike(@PathVariable @Min(1) long filmId, @PathVariable @Min(1) long userId) {
        return filmLikesService.deleteLike(filmId, userId);
    }

}
