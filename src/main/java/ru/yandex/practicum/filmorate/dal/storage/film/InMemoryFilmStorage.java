package ru.yandex.practicum.filmorate.dal.storage.film;

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
    private final Map<Long, Film> films = new HashMap<>();

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
    public Film update(Film updatedFilm) {
        log.info("Обновление фильма с id = {}", updatedFilm.getId());
        if (films.containsKey(updatedFilm.getId())) {
            Film film = films.get(updatedFilm.getId());
            film.setName(updatedFilm.getName());
            film.setDescription(updatedFilm.getDescription());
            film.setReleaseDate(updatedFilm.getReleaseDate());
            film.setDuration(updatedFilm.getDuration());
            log.info("Фильм успешно обновлен, id = {}", film.getId());
            return film;
        }
        throw new NotFoundException("Фильм с id = " + updatedFilm.getId() + " не был найден");
    }

    @Override
    public boolean delete(long filmId) {
        Film deleted = films.remove(filmId);
        return deleted != null;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getById(long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с id = {} не найден.");
        }
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void addLike(Film film, long userId) {
        log.info("Добавление лайка пользователем {} к фильму {}", userId, film.getId());
        film.addLike(userId);
        log.info("Лайк успешно добавлен");
    }

    @Override
    public void deleteLike(Film film, long userId) {
        log.info("Удаление лайка пользователем {} из фильма {}", userId, film.getId());
        film.removeLike(userId);
        log.info("Лайк успешно удален");
    }

    @Override
    public List<Film> getPopularFilmsByCount(long count) {
        log.info("Получение списка популярных фильмов в количестве {}", count);
        return films.values().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
