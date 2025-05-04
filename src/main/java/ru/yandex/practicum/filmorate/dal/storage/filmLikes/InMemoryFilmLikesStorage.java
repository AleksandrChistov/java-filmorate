package ru.yandex.practicum.filmorate.dal.storage.filmLikes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.model.FilmLikes;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class InMemoryFilmLikesStorage implements FilmLikesStorage {
    private final Map<Long, Set<Long>> filmLikes = new HashMap<>();

    @Override
    public void addLike(long filmId, long userId) {
        log.info("Добавление лайка пользователем {} к фильму {}", userId, filmId);
        Set<Long> likes = filmLikes.getOrDefault(filmId, new HashSet<>());
        if (likes.isEmpty()) {
            throw new NotFoundException("Фильм не найден с ID = " + filmId);
        }
        likes.add(userId);
        log.info("Лайк успешно добавлен");
    }

    @Override
    public boolean deleteLike(long filmId, long userId) {
        log.info("Удаление лайка пользователем {} из фильма {}", userId, filmId);
        Set<Long> likes = filmLikes.getOrDefault(filmId, new HashSet<>());
        if (likes.isEmpty()) {
            throw new NotFoundException("Фильм не найден с ID = " + filmId);
        }
        boolean isDeleted = likes.remove(userId);
        if (isDeleted) {
            log.info("Лайк успешно удален");
        }
        return isDeleted;
    }

    @Override
    public List<FilmLikes> getLikesByFilmId(long filmId) {
        log.info("Получение списка лайков по фильму с ID: {}", filmId);
        return filmLikes.getOrDefault(filmId, new HashSet<>()).stream()
                .map(userId -> new FilmLikes(filmId, userId))
                .collect(Collectors.toList());
    }
}
