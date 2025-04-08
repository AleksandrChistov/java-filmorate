package ru.yandex.practicum.filmorate.controller;

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
public class FilmController {
    private final FilmService filmService;

    public static final String URL = "/films";

    @GetMapping
    public List<Film> findAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable int id) {
        return filmService.getById(id);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilmsByCount(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilmsByCount(count);
    }

    @PostMapping
    public Film create(@Validated(Group.OnCreate.class) @RequestBody Film newFilm) {
        return filmService.create(newFilm);
    }

    @PutMapping
    public Film update(@Validated(Group.OnUpdate.class) @RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }

}
