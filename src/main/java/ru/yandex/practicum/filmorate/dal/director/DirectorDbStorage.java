package ru.yandex.practicum.filmorate.dal.director;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.DirectorRowMapper;
import ru.yandex.practicum.filmorate.dal.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

@Repository
public class DirectorDbStorage extends BaseDbStorage<Director> implements DirectorStorage {
    private static final String INSERT_DIRECTOR_QUERY = "INSERT INTO directors (name) VALUES (?)";
    private static final String FIND_ALL_DIRECTORS_QUERY = "SELECT * FROM directors";
    private static final String FIND_DIRECTOR_BY_ID_QUERY = "SELECT * FROM directors WHERE id = ?";
    private static final String UPDATE_DIRECTOR_QUERY = "UPDATE directors SET name = ? WHERE id = ?";
    private static final String DELETE_DIRECTOR_BY_ID_QUERY = "DELETE FROM directors WHERE id = ?";

    private final DirectorRowMapper mapper;

    public DirectorDbStorage(JdbcTemplate jdbc, DirectorRowMapper mapper) {
        super(jdbc);
        this.mapper = mapper;
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
        update(UPDATE_DIRECTOR_QUERY, director.getName());
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
}
