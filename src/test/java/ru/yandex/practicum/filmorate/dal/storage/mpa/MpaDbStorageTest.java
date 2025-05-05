package ru.yandex.practicum.filmorate.dal.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.enums.RatingMPA;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDbStorage.class, MpaRowMapper.class})
class MpaDbStorageTest {
    private final MpaDbStorage mpaStorage;

    @Test
    void getAll() {
        List<Mpa> mpaList = mpaStorage.getAll();

        assertThat(mpaList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    void getById() {
        Optional<Mpa> mpaOptional = mpaStorage.getById(2);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa -> {
                    assertThat(mpa).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(mpa).hasFieldOrPropertyWithValue("name", RatingMPA.PG13);
                });
    }
}