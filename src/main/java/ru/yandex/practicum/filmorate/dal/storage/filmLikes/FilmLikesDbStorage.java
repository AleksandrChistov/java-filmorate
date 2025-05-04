package ru.yandex.practicum.filmorate.dal.storage.filmLikes;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.FilmLikes;

import java.util.List;

@Repository
public class FilmLikesDbStorage extends BaseDbStorage<FilmLikes> implements FilmLikesStorage {
    private static final String INSERT_LIKE_QUERY = "INSERT films_likes (film_id, user_id) VALUES (?, ?) returning id";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
    private static final String FIND_LIKES_BY_FILM_ID_QUERY = "SELECT * FROM films_likes WHERE film_id = ?";

    public FilmLikesDbStorage(JdbcTemplate jdbc, RowMapper<FilmLikes> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addLike(long filmId, long userId) {
        insert(INSERT_LIKE_QUERY, filmId, userId);
    }

    @Override
    public boolean deleteLike(long filmId, long userId) {
        return delete(DELETE_LIKE_QUERY, filmId, userId);
    }

    @Override
    public List<FilmLikes> getLikesByFilmId(long filmId) {
        return findMany(FIND_LIKES_BY_FILM_ID_QUERY, filmId);
    }
}
