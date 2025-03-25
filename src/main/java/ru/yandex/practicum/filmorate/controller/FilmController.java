package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepton.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.CommonUtil.getNextId;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final Instant MAX_RELEASE_DATE = Instant.parse("1895-12-28T00:00:00.00Z");

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film newFilm) {
        validate(newFilm);
        newFilm.setId(getNextId(films));
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        validate(newFilm);
        if (newFilm.getId() == null) {
            throw new ValidationException("id должно быть заполнено");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            return oldFilm;
        }
        throw new ValidationException("Фильм с id = " + newFilm.getId() + " не был найден");
    }

    private void validate(Film newFilm) {
        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым");
        }
        if (newFilm.getDescription() == null || newFilm.getDescription().isBlank() || newFilm.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть пустым или больше 200 символов");
        }
        if (newFilm.getReleaseDate() == null || newFilm.getReleaseDate().isBefore(MAX_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не может быть пустой или раньше 28 декабря 1895 года");
        }
        if (newFilm.getDuration() == null || newFilm.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность фильма не может быть пустым или меньше 0");
        }
    }

}
