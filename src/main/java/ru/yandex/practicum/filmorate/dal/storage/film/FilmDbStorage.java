package ru.yandex.practicum.filmorate.dal.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.util.*;

@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String INSERT_FILM_QUERY = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?, " +
            "release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
    private static final String DELETE_FILM_BY_ID_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String FIND_ALL_FILMS_WITH_MPA_QUERY = "SELECT " +
            "f.id, f.name, f.description, f.release_date, f.duration, " +
            "mpa.id AS mpa_id, mpa.name AS mpa_name, " +
            "FROM films f " +
            "LEFT JOIN mpa ON mpa.id = f.mpa_id";
    private static final String FIND_FILM_WITH_MPA_BY_ID_QUERY = "SELECT " +
            "f.id, f.name, f.description, f.release_date, f.duration, " +
            "mpa.id AS mpa_id, mpa.name AS mpa_name FROM films f " +
            "LEFT JOIN mpa ON mpa.id = f.mpa_id " +
            "WHERE f.id = ?";
    private static final String FIND_POPULAR_FILMS_BY_COUNT_QUERY = "SELECT " +
            "f.id, f.name, f.description, f.release_date, f.duration, " +
            "mpa.id AS mpa_id, mpa.name AS mpa_name, " +
            "COUNT(fl.film_id) AS likes_count " +
            "FROM films f " +
            "LEFT JOIN mpa ON mpa.id = f.mpa_id " +
            "LEFT JOIN films_likes fl ON f.id = fl.film_id " +
            "GROUP BY f.id " +
            "ORDER BY likes_count DESC " +
            "LIMIT ?";
    private static final String FIND_POPULAR_FILMS_BY_GENRE_BY_YEAR_QUERY = "SELECT " +
            "f.id, f.name, f.description, f.release_date, f.duration, " +
            "mpa.id AS mpa_id, mpa.name AS mpa_name, " +
            "COUNT(fl.film_id) AS likes_count " +
            "FROM films f " +
            "LEFT JOIN mpa ON mpa.id = f.mpa_id " +
            "LEFT JOIN films_likes fl ON f.id = fl.film_id " +
            "LEFT JOIN films_genres fg ON f.id = fg.film_id " +
            "WHERE fg.genre_id = ? " +
            "AND EXTRACT(YEAR FROM f.release_date) = ? " +
            "GROUP BY f.id " +
            "ORDER BY likes_count DESC " +
            "LIMIT ?";
    private static final String FIND_POPULAR_FILMS_BY_GENRE_QUERY = "SELECT " +
            "f.id, f.name, f.description, f.release_date, f.duration, " +
            "mpa.id AS mpa_id, mpa.name AS mpa_name, " +
            "COUNT(fl.film_id) AS likes_count " +
            "FROM films f " +
            "LEFT JOIN mpa ON mpa.id = f.mpa_id " +
            "LEFT JOIN films_likes fl ON f.id = fl.film_id " +
            "LEFT JOIN films_genres fg ON f.id = fg.film_id " +
            "WHERE fg.genre_id = ? " +
            "GROUP BY f.id " +
            "ORDER BY likes_count DESC " +
            "LIMIT ?";
    private static final String FIND_POPULAR_FILMS_BY_YEAR_QUERY = "SELECT " +
            "f.id, f.name, f.description, f.release_date, f.duration, " +
            "mpa.id AS mpa_id, mpa.name AS mpa_name, " +
            "COUNT(fl.film_id) AS likes_count " +
            "FROM films f " +
            "LEFT JOIN mpa ON mpa.id = f.mpa_id " +
            "LEFT JOIN films_likes fl ON f.id = fl.film_id " +
            "LEFT JOIN films_genres fg ON f.id = fg.film_id " +
            "WHERE EXTRACT(YEAR FROM f.release_date) = ? " +
            "GROUP BY f.id " +
            "ORDER BY likes_count DESC " +
            "LIMIT ?";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO films_likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
    private static final String FIND_LIKES_BY_FILM_ID_QUERY = "SELECT user_id FROM films_likes WHERE film_id = ?";
    private static final String FIND_LIKES_BY_FILM_IDS_QUERY = "SELECT user_id, film_id FROM films_likes WHERE film_id IN (:filmsIds)";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<Film> mapper;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbc);
        this.mapper = mapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Film> getAll() {
        return findMany(FIND_ALL_FILMS_WITH_MPA_QUERY, mapper);
    }

    @Override
    public Film add(Film newFilm) {
        long id = insert(
                INSERT_FILM_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId()
        );
        newFilm.setId(id);
        return newFilm;
    }

    @Override
    public Film update(Film updatedFilm) {
        update(
                UPDATE_FILM_QUERY,
                updatedFilm.getName(),
                updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(),
                updatedFilm.getDuration(),
                updatedFilm.getMpa().getId(),
                updatedFilm.getId()
        );
        return updatedFilm;
    }

    @Override
    public boolean delete(long filmId) {
        return delete(DELETE_FILM_BY_ID_QUERY, filmId);
    }

    @Override
    public Optional<Film> getById(long filmId) {
        return findOne(FIND_FILM_WITH_MPA_BY_ID_QUERY, mapper, filmId);
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
    public List<Long> getLikesByFilmId(long filmId) {
        return jdbc.queryForList(FIND_LIKES_BY_FILM_ID_QUERY, Long.class, filmId);
    }

    @Override
    public Map<Long, List<Long>> getLikesByFilmIds(List<Long> filmsIds) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("filmsIds", filmsIds);

        Map<Long, List<Long>> filmLikesMap = new HashMap<>();

        namedParameterJdbcTemplate.query(FIND_LIKES_BY_FILM_IDS_QUERY, parameters, (ResultSet rs, int rowNum) -> {
            Long userId = rs.getLong("user_id");
            Long filmId = rs.getLong("film_id");
            filmLikesMap.computeIfAbsent(filmId, k -> new ArrayList<>()).add(userId);
            return null;
        });

        return filmLikesMap;
    }

    @Override
    public List<Film> getPopularFilmsByCount(int count) {
        return findMany(FIND_POPULAR_FILMS_BY_COUNT_QUERY, mapper, count);
    }

    @Override
    public List<Film> findPopularFilmsByGenreByYear(int count, Long genreId, Long year) {
        return findMany(FIND_POPULAR_FILMS_BY_GENRE_BY_YEAR_QUERY, mapper, genreId, year, count);
    }

    @Override
    public List<Film> findPopularFilmsByGenre(int count, Long genreId) {
        return findMany(FIND_POPULAR_FILMS_BY_GENRE_QUERY, mapper, genreId, count);
    }

    @Override
    public List<Film> findPopularFilmsByYear(int count, Long year) {
        return findMany(FIND_POPULAR_FILMS_BY_YEAR_QUERY, mapper, year, count);
    }
}
