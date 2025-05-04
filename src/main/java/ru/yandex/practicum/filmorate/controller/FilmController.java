package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.Group;

import java.util.List;

@RestController
@RequestMapping(FilmController.URL)
@RequiredArgsConstructor
@Validated
public class FilmController {
    private final FilmService filmService;

    public static final String URL = "/films";

    @GetMapping
    public List<Film> findAll() {
        return filmService.getAll();
    }

    @GetMapping("/{filmId}")
    public Film findById(@PathVariable @Min(1) long filmId) {
        return filmService.getById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilmsByCount(@RequestParam(defaultValue = "10") long count) {
        return filmService.getPopularFilmsByCount(count);
    }

    @PostMapping
    public Film create(@Validated(Group.OnCreate.class) @RequestBody Film newFilm) {
        return filmService.create(newFilm);
    }

    @PutMapping("/{filmId}")
    public Film update(
            @PathVariable long filmId,
            @Validated(Group.OnUpdate.class) @RequestBody Film newFilm) {
        return filmService.update(filmId, newFilm);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable @Min(1) long filmId, @PathVariable @Min(1) long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable @Min(1) long filmId, @PathVariable @Min(1) long userId) {
        filmService.deleteLike(filmId, userId);
    }

}
