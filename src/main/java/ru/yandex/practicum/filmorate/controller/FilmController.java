package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.Group;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.CommonUtil.getNextId;

@RestController
@RequestMapping(FilmController.URL)
@Slf4j
public class FilmController {
    public static final String URL = "/films";
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Validated(Group.OnCreate.class) @RequestBody Film newFilm) {
        log.info("Добавление нового фильма с названием = {}", newFilm.getName());
        newFilm.setId(getNextId(films));
        log.debug("Фильму был присвоен id = {}", newFilm.getId());
        films.put(newFilm.getId(), newFilm);
        log.info("Фильм успешно добавлен, id = {}", newFilm.getId());
        return newFilm;
    }

    @PutMapping
    public Film update(@Validated(Group.OnUpdate.class) @RequestBody Film newFilm) {
        log.info("Обновление фильма с id = {}", newFilm.getId());
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Фильм успешно обновлен, id = {}", oldFilm.getId());
            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не был найден");
    }

}
