package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.excepton.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
    }

    @Test
    void createWithEmailValidationException() {
        User userWithNullEmail = new User(null, "User Name", "userLogin", null, LocalDate.now());
        User userWithEmptyEmail = new User(null, "User Name", "userLogin", "", LocalDate.now());
        User userWithWrongEmail = new User(null, "User Name", "userLogin", "some-email.ru", LocalDate.now());

        assertThrows(ValidationException.class, () -> userController.create(userWithNullEmail), "Тип ошибки не совпадает при email = NULL");
        assertThrows(ValidationException.class, () -> userController.create(userWithEmptyEmail), "Тип ошибки не совпадает при пустом email");
        assertThrows(ValidationException.class, () -> userController.create(userWithWrongEmail), "Тип ошибки не совпадает при email без @");
    }

    @Test
    void createWithLoginValidationException() {
        User userWithNullLogin = new User(null, "User Name", null, "email@mail.ru", LocalDate.now());
        User userWithEmptyLogin = new User(null, "User Name", "", "email@mail.ru", LocalDate.now());
        User userWithSpacesLogin = new User(null, "User Name", "  ", "email@mail.ru", LocalDate.now());

        assertThrows(ValidationException.class, () -> userController.create(userWithNullLogin), "Тип ошибки не совпадает при логине = NULL");
        assertThrows(ValidationException.class, () -> userController.create(userWithEmptyLogin), "Тип ошибки не совпадает при пустом логине");
        assertThrows(ValidationException.class, () -> userController.create(userWithSpacesLogin), "Тип ошибки не совпадает при логине с пустыми пробелами");
    }

    @Test
    void createWithBirthdayValidationException() {
        User userWithNullBirthday = new User(null, "User name", "userLogin", "email@mail.ru", null);
        User userWithBirthdayInFuture = new User(null, "User name", "userLogin", "email@mail.ru", LocalDate.now().plusYears(1));

        assertThrows(ValidationException.class, () -> userController.create(userWithNullBirthday), "Тип ошибки не совпадает при birthday = NULL");
        assertThrows(ValidationException.class, () -> userController.create(userWithBirthdayInFuture), "Тип ошибки не совпадает при birthday в будущем");
    }

    @Test
    void createWithEmptyName() {
        User userWithNullName = new User(null, null, "userLogin", "email@mail.ru", LocalDate.now());
        User userWithEmptyName = new User(null, "", "userLogin", "email@mail.ru", LocalDate.now());

        User createdWithNullName = userController.create(userWithNullName);
        User createdWithEmptyName = userController.create(userWithEmptyName);

        assertEquals("userLogin", createdWithNullName.getName(), "Имя не совпадает с логином при имени = NULL");
        assertEquals("userLogin", createdWithEmptyName.getName(), "Имя не совпадает с логином при пустом имени");
    }

}