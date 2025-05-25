package ru.yandex.practicum.filmorate.dal.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.util.*;

@Repository
public class GenreDbStorage extends BaseDbStorage implements GenreStorage {
    private static final String INSERT_GENRES_BY_FILM_ID_QUERY = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String FIND_ALL_GENRES_QUERY = "SELECT * FROM genres";
    private static final String FIND_GENRE_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_GENRES_BY_FILM_IDS_QUERY = "SELECT " +
            "g.id AS id, g.name AS name, fg.film_id " +
            "FROM films_genres fg " +
            "JOIN genres g ON g.id = fg.genre_id " +
            "WHERE fg.film_id IN (:filmsIds) " +
            "ORDER BY g.id";
    private static final String FIND_GENRES_BY_FILM_ID_QUERY = "SELECT " +
            "g.id AS id, g.name AS name " +
            "FROM films_genres fg " +
            "JOIN genres g ON g.id = fg.genre_id " +
            "WHERE fg.film_id = ? " +
            "ORDER BY g.id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<Genre> mapper;

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbc);
        this.mapper = mapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int addGenresByFilmId(Set<Long> genresIds, long filmId) {
        List<Object[]> batch = new ArrayList<>();

        for (Long genreId : genresIds) {
            Object[] values = new Object[]{filmId, genreId};
            batch.add(values);
        }

        int[] affectedRows = jdbc.batchUpdate(INSERT_GENRES_BY_FILM_ID_QUERY, batch);

        return affectedRows.length;
    }

    @Override
    public List<Genre> getAll() {
        return findMany(FIND_ALL_GENRES_QUERY, mapper);
    }

    @Override
    public Optional<Genre> getById(long filmId) {
        return findOne(FIND_GENRE_BY_ID_QUERY, mapper, filmId);
    }

    @Override
    public Map<Long, Set<Genre>> getAllByFilmIds(List<Long> filmsIds) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("filmsIds", filmsIds);

        Map<Long, Set<Genre>> filmGenresMap = new HashMap<>();

        namedParameterJdbcTemplate.query(FIND_GENRES_BY_FILM_IDS_QUERY, parameters, (ResultSet rs, int rowNum) -> {
            Genre genre = new Genre(rs.getLong("id"), rs.getString("name"));
            Long filmId = rs.getLong("film_id");
            filmGenresMap.computeIfAbsent(filmId, k -> new LinkedHashSet<>()).add(genre);
            return null;
        });

        return filmGenresMap;
    }

    @Override
    public List<Genre> getAllByFilmId(long filmsId) {
        return findMany(FIND_GENRES_BY_FILM_ID_QUERY, mapper, filmsId);
    }
}
