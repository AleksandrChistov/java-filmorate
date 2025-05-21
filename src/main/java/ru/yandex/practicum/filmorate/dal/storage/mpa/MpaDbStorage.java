package ru.yandex.practicum.filmorate.dal.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage extends BaseDbStorage<Mpa> implements MpaStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";

    private final RowMapper<Mpa> mapper;

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc);
        this.mapper = mapper;
    }

    @Override
    public List<Mpa> getAll() {
        return findMany(FIND_ALL_QUERY, mapper);
    }

    @Override
    public Optional<Mpa> getById(long mpaId) {
        return findOne(FIND_BY_ID_QUERY, mapper, mpaId);
    }
}
