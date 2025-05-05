package ru.yandex.practicum.filmorate.dal.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mapper.FilmLikesRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.storage.filmLikes.FilmLikesDbStorage;
import ru.yandex.practicum.filmorate.dal.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLikes;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmRowMapper.class, FilmLikesDbStorage.class, FilmLikesRowMapper.class, GenreDbStorage.class, GenreRowMapper.class})
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final FilmLikesDbStorage filmLikesStorage;
    private final GenreDbStorage genreStorage;

    @Test
    void add() {
        Film addedFilm = new Film(null, "Хомяк Байт", "Фильм про хомяка",
                LocalDate.of(2025, Month.DECEMBER, 1), 90, 1L);
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
        Film updatedFilm = new Film(1L, "Дурак", "Фильм про дурака", expectedNewReleaseDate, 120, 1L);

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

        List<FilmLikes> likes = filmLikesStorage.getLikesByFilmId(2);
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
}