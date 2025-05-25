package ru.yandex.practicum.filmorate.dal.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.util.CommonUtil.getNextId;

@Repository
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
    public List<Film> getPopularFilmsByCount(int count) {
        return getAll().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikesIds().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<Long>> getLikesByFilmIds(List<Long> filmIds) {
        Map<Long, List<Long>> filmIdLikesMap = new HashMap<>();

        filmIds.forEach(filmId -> {
            List<Long> likes = getLikesByFilmId(filmId);
            filmIdLikesMap.put(filmId, likes);
        });

        return filmIdLikesMap;
    }

    @Override
    public List<Long> getLikesByFilmId(long filmId) {
        log.info("Получение списка лайков по фильму с ID: {}", filmId);
        Film film = films.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм не найден с ID = " + filmId);
        }
        return new ArrayList<>(film.getLikesIds());
    }

    @Override
    public void addLike(long filmId, long userId) {
        log.info("Добавление лайка пользователем {} к фильму {}", userId, filmId);
        Film film = films.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм не найден с ID = " + filmId);
        }
        Set<Long> likes = film.getLikesIds();
        likes.add(userId);
        log.info("Лайк успешно добавлен");
    }

    @Override
    public boolean deleteLike(long filmId, long userId) {
        log.info("Удаление лайка пользователем {} из фильма {}", userId, filmId);
        Film film = films.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм не найден с ID = " + filmId);
        }
        boolean isDeleted = film.getLikesIds().remove(userId);
        if (isDeleted) {
            log.info("Лайк успешно удален");
        }
        return isDeleted;
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        return List.of();
    }

    @Override
    public List<Film> findPopularFilmsByGenreByYear(int count, Long genreId, Long year) {
        return null;
    }

    @Override
    public List<Film> findPopularFilmsByGenre(int count, Long genreId) {
        return null;
    }

    @Override
    public List<Film> findPopularFilmsByYear(int count, Long year) {
        return null;
    }

    @Override
    public List<Film> getFilmsDirectorLikes(Long directorId) {
        return List.of();
    }

    @Override
    public List<Film> getFilmsDirectorYear(Long directorId) {
        return List.of();
    }
}
