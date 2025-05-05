package ru.yandex.practicum.filmorate.dal.storage.filmLikes;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mapper.FilmLikesRowMapper;
import ru.yandex.practicum.filmorate.model.FilmLikes;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmLikesDbStorage.class, FilmLikesRowMapper.class})
class FilmLikesDbStorageTest {
    private final FilmLikesDbStorage filmLikesStorage;

    @Test
    void addLike() {
        filmLikesStorage.addLike(1, 3);

        List<FilmLikes> filmLikes = filmLikesStorage.getLikesByFilmId(1);

        assertThat(filmLikes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        assertThat(filmLikes.get(1))
                .hasFieldOrPropertyWithValue("userId", 3L);
    }

    @Test
    void deleteLike() {
        boolean deleted = filmLikesStorage.deleteLike(1, 1);

        assertThat(deleted).isTrue();

        List<FilmLikes> filmLikes = filmLikesStorage.getLikesByFilmId(1);

        assertThat(filmLikes).isEmpty();
    }

    @Test
    void getLikesByFilmId() {
        List<FilmLikes> filmLikes = filmLikesStorage.getLikesByFilmId(2);

        assertThat(filmLikes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        assertThat(filmLikes)
                .extracting("userId")
                .containsExactlyInAnyOrder(1L, 2L);
    }
}