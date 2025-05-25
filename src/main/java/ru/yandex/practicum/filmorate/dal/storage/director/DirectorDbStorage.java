package ru.yandex.practicum.filmorate.dal.storage.director;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dto.DirectorDto;
import ru.yandex.practicum.filmorate.dal.mapper.DirectorRowMapper;
import ru.yandex.practicum.filmorate.dal.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.util.*;

@Repository
public class DirectorDbStorage extends BaseDbStorage implements DirectorStorage {
    private static final String INSERT_DIRECTOR_QUERY = "INSERT INTO directors (name) VALUES (?)";
    private static final String INSERT_FILMS_DIRECTORS_QUERY = "INSERT INTO films_directors (film_id, director_id) VALUES (?, ?)";
    private static final String FIND_ALL_DIRECTORS_QUERY = "SELECT * FROM directors";
    private static final String FIND_DIRECTOR_BY_ID_QUERY = "SELECT * FROM directors WHERE id = ?";
    private static final String UPDATE_DIRECTOR_QUERY = "UPDATE directors SET name = ? WHERE id = ?";
    private static final String DELETE_DIRECTOR_BY_ID_QUERY = "DELETE FROM directors WHERE id = ?";
    private static final String FIND_DIRECTORS_IDS_QUERY = "SELECT d.id AS id, d.name AS name, fd.film_id " +
            "FROM films_directors AS fd " +
            "JOIN directors AS d ON d.id = fd.director_id " +
            "WHERE fd.film_id IN (:filmsIds) " +
            "ORDER BY d.id";
    private static final String FIND_DIRECTOR_ID_QUERY = "SELECT d.id AS id, d.name AS name, fd.film_id " +
            "FROM films_directors AS fd " +
            "JOIN directors AS d ON d.id = fd.director_id " +
            "WHERE fd.film_id = ? " +
            "ORDER BY d.id";

    private final DirectorRowMapper mapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DirectorDbStorage(JdbcTemplate jdbc, DirectorRowMapper mapper,
                             NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbc);
        this.mapper = mapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void addDirectorsByFilmId(Film film) {
        for (DirectorDto director : film.getDirectors()) {
            jdbc.update(INSERT_FILMS_DIRECTORS_QUERY, film.getId(), director.getId());
        }
    }

    @Override
    public List<Director> getAll() {
        return findMany(FIND_ALL_DIRECTORS_QUERY, mapper);
    }

    @Override
    public Director add(Director director) {
        long id = insert(INSERT_DIRECTOR_QUERY, director.getName());
        director.setId(id);
        return director;
    }

    @Override
    public Director update(Director director) {
        update(UPDATE_DIRECTOR_QUERY, director.getName(), director.getId());
        return director;
    }

    @Override
    public void remove(Long id) {
        jdbc.update(DELETE_DIRECTOR_BY_ID_QUERY, id);

    }

    @Override
    public Optional<Director> getById(Long id) {
        return findOne(FIND_DIRECTOR_BY_ID_QUERY, mapper, id);
    }

    @Override
    public Map<Long, Set<Director>> getAllByFilmIds(List<Long> filmsIds) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("filmsIds", filmsIds);

        Map<Long, Set<Director>> filmDirectorsMap = new HashMap<>();

        namedParameterJdbcTemplate.query(FIND_DIRECTORS_IDS_QUERY, parameters, (ResultSet rs, int rowNum) -> {
            Director director = new Director(rs.getLong("id"), rs.getString("name"));
            Long filmId = rs.getLong("film_id");
            filmDirectorsMap.computeIfAbsent(filmId, k -> new LinkedHashSet<>()).add(director);
            return null;
        });

        return filmDirectorsMap;
    }

    @Override
    public List<Director> getAllByFilmId(long filmId) {
        return findMany(FIND_DIRECTOR_ID_QUERY, mapper, filmId);
    }
}
