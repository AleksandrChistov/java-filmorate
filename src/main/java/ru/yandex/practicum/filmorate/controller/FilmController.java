package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.excepton.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.CommonUtil.getNextId;

@RestController
@RequestMapping(FilmController.URL)
@Slf4j
public class FilmController {
    public static final String URL = "/films";
    public static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film newFilm) {
        try {
            log.info("Добавление нового фильма с названием = {}", newFilm.getName());
            newFilm.setId(getNextId(films));
            log.debug("Фильму был присвоен id = {}", newFilm.getId());
            films.put(newFilm.getId(), newFilm);
            log.info("Фильм успешно добавлен, id = {}", newFilm.getId());
            return newFilm;
        } catch (ValidationException e) {
            log.warn("Ошибка валидации при добавлении фильма: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при добавлении фильма: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при добавлении фильма");
        }
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        try {
            log.info("Обновление фильма с id = {}", newFilm.getId());
            if (newFilm.getId() == null) {
                throw new ValidationException("id должно быть заполнено");
            }
            log.debug("id фильма: {}", newFilm.getId());
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
        } catch (ValidationException e) {
            log.warn("Ошибка валидации при обновлении фильма: {}", e.getMessage());
            throw e;
        } catch (NotFoundException e) {
            log.warn("Ошибка при обновлении фильма: {}", e.getMessage());
            throw e;
        }  catch (Exception e) {
            log.error("Непредвиденная ошибка при обновлении фильма: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при обновлении фильма");
        }
    }

}
