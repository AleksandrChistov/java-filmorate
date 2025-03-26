package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.excepton.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void createWithNameValidationException() {
        Film filmWithNullName = new Film(null, null, "Film description", LocalDate.now(), 60);
        Film filmWithEmptyName = new Film(null, "", "Film description", LocalDate.now(), 60);

        assertThrows(ValidationException.class, () -> filmController.create(filmWithNullName), "Тип ошибки не совпадает при name = NULL");
        assertThrows(ValidationException.class, () -> filmController.create(filmWithEmptyName), "Тип ошибки не совпадает при пустом name");
    }

    @Test
    void createWithDescriptionValidationException() {
        Film filmWithNullDescription = new Film(null, "Film Name", null, LocalDate.now(), 60);
        Film filmWithEmptyDescription = new Film(null, "Film Name", "", LocalDate.now(), 60);
        String chars201 = "Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description overall 201 ch";
        Film filmWithMoreThan200CharsDescription = new Film(null, "Film Name", chars201, LocalDate.now(), 60);

        assertThrows(ValidationException.class, () -> filmController.create(filmWithNullDescription), "Тип ошибки не совпадает при description = NULL");
        assertThrows(ValidationException.class, () -> filmController.create(filmWithEmptyDescription), "Тип ошибки не совпадает при пустом description");
        assertThrows(ValidationException.class, () -> filmController.create(filmWithMoreThan200CharsDescription), "Тип ошибки не совпадает при description более 200 символов");
    }

    @Test
    void createWithReleaseDateValidationException() {
        Film filmWithNullReleaseDate = new Film(null, "Film name", "Film description", null, 60);
        Film filmWithReleaseDateBeforeMinDate = new Film(null, "Film name", "Film description", FilmController.MIN_RELEASE_DATE.minusDays(1), 60);

        assertThrows(ValidationException.class, () -> filmController.create(filmWithNullReleaseDate), "Тип ошибки при releaseDate = NULL не совпадает");
        assertThrows(ValidationException.class, () -> filmController.create(filmWithReleaseDateBeforeMinDate), "Тип ошибки при releaseDate после максимальной даты релиза не совпадает");
    }

    @Test
    void createWithDurationValidationException() {
        Film filmWithNullDuration = new Film(null, "Film name", "Film description", LocalDate.now(), null);
        Film filmWithNegativeDuration = new Film(null, "Film name", "Film description", LocalDate.now(), -1);

        assertThrows(ValidationException.class, () -> filmController.create(filmWithNullDuration), "Тип ошибки не совпадает при duration = NULL");
        assertThrows(ValidationException.class, () -> filmController.create(filmWithNegativeDuration), "Тип ошибки не совпадает при duration меньше нуля");
    }

}