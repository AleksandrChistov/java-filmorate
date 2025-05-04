package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.storage.filmLikes.FilmLikesStorage;
import ru.yandex.practicum.filmorate.model.FilmLikes;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmLikesService {
    private final FilmLikesStorage filmLikesStorage;

    public FilmLikesService(@Qualifier("filmLikesDbStorage") FilmLikesStorage filmLikesStorage) {
        this.filmLikesStorage = filmLikesStorage;
    }

    public void addLike(long filmId, long userId) {
        log.info("Добавление лайка пользователем {} к фильму {}", userId, filmId);
        filmLikesStorage.addLike(filmId, userId);
        log.info("Лайк успешно добавлен");
    }

    public boolean deleteLike(long filmId, long userId) {
        log.info("Удаление лайка пользователем {} из фильма {}", userId, userId);
        boolean isDeleted = filmLikesStorage.deleteLike(filmId, userId);
        if (isDeleted) {
            log.info("Лайк успешно удален");
        }
        return isDeleted;
    }

    public Set<Long> getLikesByFilmId(long filmId) {
        log.info("Получение списка лайков по фильму с ID {}", filmId);
        return filmLikesStorage.getLikesByFilmId(filmId).stream()
                .map(FilmLikes::getUserId)
                .collect(Collectors.toSet());
    }
}
