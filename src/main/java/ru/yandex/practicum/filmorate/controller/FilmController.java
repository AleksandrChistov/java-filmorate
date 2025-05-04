package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
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
    public List<FilmDto> findAll() {
        return filmService.getAll();
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto findById(@PathVariable @Min(1) long filmId) {
        return filmService.getById(filmId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<FilmDto> findPopularFilmsByCount(@RequestParam(defaultValue = "10") long count) {
        return filmService.getPopularFilmsByCount(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto create(@Validated @RequestBody FilmDto newFilm) {
        return filmService.create(newFilm);
    }

    @PutMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto update(
            @PathVariable long filmId,
            @Validated @RequestBody FilmDto newFilm) {
        return filmService.update(filmId, newFilm);
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
