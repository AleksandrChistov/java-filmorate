package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmLikes;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmLikesRowMapper implements RowMapper<FilmLikes> {
    @Override
    public FilmLikes mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FilmLikes(
                rs.getLong("film_id"),
                rs.getLong("user_id")
        );
    }
}
