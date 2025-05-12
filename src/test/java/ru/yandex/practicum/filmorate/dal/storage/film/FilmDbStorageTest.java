package ru.yandex.practicum.filmorate.dal.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.dto.MpaDto;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmRowMapper.class, GenreDbStorage.class, GenreRowMapper.class})
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;

    @Test
    void add() {
        Film addedFilm = new Film(null, "Хомяк Байт", "Фильм про хомяка",
                LocalDate.of(2025, Month.DECEMBER, 1), 90, new MpaDto(1L));
        Film added = filmStorage.add(addedFilm);

        assertThat(added).isNotNull().hasFieldOrPropertyWithValue("id", 4L);

        Optional<Film> filmOptional = filmStorage.getById(4);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 4L)
                );
    }

    @Test
    void update() {
        LocalDate expectedNewReleaseDate = LocalDate.of(2026, Month.FEBRUARY, 20);
        Film updatedFilm = new Film(1L, "Дурак", "Фильм про дурака", expectedNewReleaseDate, 120, new MpaDto(1L));

        filmStorage.update(updatedFilm);

        Optional<Film> filmOptional = filmStorage.getById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("releaseDate", expectedNewReleaseDate)
                );

    }

    @Test
    void delete() {
        boolean deleted = filmStorage.delete(2);

        assertThat(deleted).isTrue();

        Optional<Film> filmOptional = filmStorage.getById(2);
        assertThat(filmOptional).isEmpty();

        List<Long> likes = filmStorage.getLikesByFilmId(2);
        assertThat(likes).isEmpty();

        List<Genre> genres = genreStorage.getAllByFilmId(2);
        assertThat(genres).isEmpty();
    }

    @Test
    void getAll() {
        List<Film> films = filmStorage.getAll();

        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);
    }

    @Test
    void getById() {
        Optional<Film> filmOptional = filmStorage.getById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    void addLike() {
        filmStorage.addLike(1, 3);

        List<Long> filmLikes = filmStorage.getLikesByFilmId(1);

        assertThat(filmLikes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsOnlyOnce(3L);
    }

    @Test
    void deleteLike() {
        boolean deleted = filmStorage.deleteLike(1, 1);

        assertThat(deleted).isTrue();

        List<Long> filmLikes = filmStorage.getLikesByFilmId(1);

        assertThat(filmLikes).isEmpty();
    }

    @Test
    void getLikesByFilmId() {
        List<Long> filmLikes = filmStorage.getLikesByFilmId(2);

        assertThat(filmLikes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(1L, 2L);
    }
}