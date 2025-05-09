package ru.yandex.practicum.filmorate.dal.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreDbStorage.class, GenreRowMapper.class})
class GenreDbStorageTest {
    private final GenreDbStorage genreStorage;

    @Test
    void getAll() {
        List<Genre> genres = genreStorage.getAll();

        assertThat(genres)
                .isNotNull()
                .isNotEmpty()
                .hasSize(6);
    }

    @Test
    void getById() {
        Optional<Genre> genreOptional = genreStorage.getById(2);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre -> {
                    assertThat(genre).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(genre).hasFieldOrPropertyWithValue("name", "Драма");
                });
    }

    @Test
    void getAllByFilmId() {
        List<Genre> genres = genreStorage.getAllByFilmId(2);

        assertThat(genres)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(genres.getFirst())
                .hasFieldOrPropertyWithValue("id", 1L);
    }
}