package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.Group;

import java.util.Collection;

@RestController
@RequestMapping(FilmController.URL)
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    public static final String URL = "/films";

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.getAll();
    }

    @PostMapping
    public Film create(@Validated(Group.OnCreate.class) @RequestBody Film newFilm) {
        return filmService.create(newFilm);
    }

    @PutMapping
    public Film update(@Validated(Group.OnUpdate.class) @RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

}
