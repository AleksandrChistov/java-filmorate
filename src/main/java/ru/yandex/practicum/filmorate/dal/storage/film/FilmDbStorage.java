package ru.yandex.practicum.filmorate.dal.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String INSERT_FILM_QUERY = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?, " +
            "release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
    private static final String DELETE_FILM_BY_ID_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String FIND_ALL_FILMS_WITH_MPA_QUERY = "SELECT " +
            "f.id, f.name, f.description, f.release_date, f.duration, " +
            "mpa.id AS mpa_id, mpa.name AS mpa_name FROM films f " +
            "LEFT JOIN mpa ON mpa.id = f.mpa_id";
    private static final String FIND_FILM_WITH_MPA_BY_ID_QUERY = "SELECT " +
            "f.id, f.name, f.description, f.release_date, f.duration, " +
            "mpa.id AS mpa_id, mpa.name AS mpa_name FROM films f " +
            "LEFT JOIN mpa ON mpa.id = f.mpa_id " +
            "WHERE f.id = ?";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
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
    public List<Film> getAll() {
        return findMany(FIND_ALL_FILMS_WITH_MPA_QUERY);
    }

    @Override
    public Optional<Film> getById(long filmId) {
        return findOne(FIND_FILM_WITH_MPA_BY_ID_QUERY, filmId);
    }
}
