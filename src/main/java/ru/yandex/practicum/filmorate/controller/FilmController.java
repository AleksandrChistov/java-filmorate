package ru.yandex.practicum.filmorate.controller;

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
@RequestMapping("/films")
@Slf4j
public class FilmController {
    public static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film newFilm) {
        try {
            log.info("Добавление нового фильма с названием = {}", newFilm.getName());
            validate(newFilm);
            log.debug("Фильм прошёл валидацию: {}", newFilm);
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
    public Film update(@RequestBody Film newFilm) {
        try {
            log.info("Обновление фильма с id = {}", newFilm.getId());
            validate(newFilm);
            if (newFilm.getId() == null) {
                throw new ValidationException("id должно быть заполнено");
            }
            log.debug("Фильм для обновления прошёл валидацию: {}", newFilm);
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

    private void validate(Film newFilm) {
        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            throw new ValidationException("Название фильмы не может быть пустым");
        }
        if (newFilm.getDescription() == null || newFilm.getDescription().isBlank() || newFilm.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть пустым или больше 200 символов");
        }
        if (newFilm.getReleaseDate() == null || newFilm.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не может быть пустой или раньше 28 декабря 1895 года");
        }
        if (newFilm.getDuration() == null || newFilm.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть пустым или меньше 0");
        }
    }

}
