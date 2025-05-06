package ru.yandex.practicum.filmorate.dal.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {
    private static final String INSERT_GENRES_BY_FILM_ID_QUERY = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String FIND_ALL_GENRES_QUERY = "SELECT * FROM genres";
    private static final String FIND_GENRE_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_GENRES_BY_FILM_ID_QUERY = "SELECT g.id AS id, g.name AS name FROM films_genres fg " +
            "LEFT JOIN genres g ON g.id = fg.genre_id " +
            "WHERE film_id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public int addGenresByFilmId(Set<Long> genresIds, long filmId) {
        List<Object[]> batch = new ArrayList<>();

        for (Long genreId : genresIds) {
            Object[] values = new Object[] {filmId, genreId};
            batch.add(values);
        }

        int[] affectedRows = jdbc.batchUpdate(INSERT_GENRES_BY_FILM_ID_QUERY, batch);

        return affectedRows.length;
    }

    @Override
    public List<Genre> getAll() {
        return findMany(FIND_ALL_GENRES_QUERY);
    }

    @Override
    public Optional<Genre> getById(long filmId) {
        return findOne(FIND_GENRE_BY_ID_QUERY, filmId);
    }

    @Override
    public List<Genre> getAllByFilmId(long filmId) {
        return findMany(FIND_GENRES_BY_FILM_ID_QUERY, filmId);
    }

}
