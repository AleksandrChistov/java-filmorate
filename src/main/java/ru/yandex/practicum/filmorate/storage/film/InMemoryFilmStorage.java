package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.util.CommonUtil.getNextId;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film add(Film newFilm) {
        log.info("Добавление нового фильма с названием = {}", newFilm.getName());
        newFilm.setId(getNextId(films));
        log.debug("Фильму был присвоен id = {}", newFilm.getId());
        films.put(newFilm.getId(), newFilm);
        log.info("Фильм успешно добавлен, id = {}", newFilm.getId());
        return newFilm;
    }

    @Override
    public Film update(Film newFilm) {
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

    @Override
    public Film delete(int filmId) {
        return films.remove(filmId);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(int id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с id = {} не найден.");
        }
        return films.get(id);
    }

    @Override
    public void addLike(Film film, int userId) {
        log.info("Добавление лайка пользователем {} к фильму {}", userId, film.getId());
        film.addLike(userId);
        log.info("Лайк успешно добавлен");
    }

    @Override
    public void deleteLike(Film film, int userId) {
        log.info("Удаление лайка пользователем {} из фильма {}", userId, film.getId());
        film.removeLike(userId);
        log.info("Лайк успешно удален");
    }

    @Override
    public List<Film> getPopularFilmsByCount(int count) {
        log.info("Получение списка популярных фильмов в количестве {}", count);
        return films.values().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
