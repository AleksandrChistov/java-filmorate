package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createWithEmailValidationException() throws Exception {
        User userWithNullEmail = new User(null, "User Name", "userLogin", null, LocalDate.now().minusDays(1));
        User userWithEmptyEmail = new User(null, "User Name", "userLogin", "", LocalDate.now().minusDays(1));
        User userWithWrongEmail = new User(null, "User Name", "userLogin", "some-email.ru", LocalDate.now().minusDays(1));

        mockMvc.perform(MockMvcRequestBuilders.post(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithNullEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при email = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.post(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithEmptyEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при пустом email"));

        mockMvc.perform(MockMvcRequestBuilders.post(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithWrongEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при email без @"));
    }

    @Test
    void createWithLoginValidationException() throws Exception {
        User userWithNullLogin = new User(null, "User Name", null, "email@mail.ru", LocalDate.now().minusDays(1));
        User userWithEmptyLogin = new User(null, "User Name", "", "email@mail.ru", LocalDate.now().minusDays(1));
        User userWithSpacesLogin = new User(null, "User Name", "  ", "email@mail.ru", LocalDate.now().minusDays(1));

        mockMvc.perform(MockMvcRequestBuilders.post(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithNullLogin)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при логине = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.post(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithEmptyLogin)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при пустом логине"));

        mockMvc.perform(MockMvcRequestBuilders.post(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithSpacesLogin)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при логине с пустыми пробелами"));
    }

    @Test
    void createWithBirthdayValidationException() throws Exception {
        User userWithNullBirthday = new User(null, "User name", "userLogin", "email@mail.ru", null);
        User userWithBirthdayInFuture = new User(null, "User name", "userLogin", "email@mail.ru", LocalDate.now().plusYears(1));

        mockMvc.perform(MockMvcRequestBuilders.post(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithNullBirthday)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при birthday = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.post(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithBirthdayInFuture)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при birthday в будущем"));
    }

    @Test
    void createWithEmptyName() throws Exception {
        User userWithNullName = new User(null, null, "userLogin", "email@mail.ru", LocalDate.now().minusDays(1));
        User userWithEmptyName = new User(null, "", "userLogin", "email@mail.ru", LocalDate.now().minusDays(1));

        String createdWithNullNameBody = mockMvc.perform(MockMvcRequestBuilders.post(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithNullName)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String createdWithEmptyNameBody = mockMvc.perform(MockMvcRequestBuilders.post(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithEmptyName)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        User createdWithNullName = objectMapper.readValue(createdWithNullNameBody, User.class);
        User createdWithEmptyName = objectMapper.readValue(createdWithEmptyNameBody, User.class);

        assertEquals("userLogin", createdWithNullName.getName(), "Имя не совпадает с логином при имени = NULL");
        assertEquals("userLogin", createdWithEmptyName.getName(), "Имя не совпадает с логином при пустом имени");
    }

    @Test
    void updateWithIdValidationException() throws Exception {
        User userWithNullId = new User(null, "User name", "userLogin", "email@mail.ru", LocalDate.now().minusDays(1));
        User userWithNotFoundId = new User(1, "User name", "userLogin", "email@mail.ru", LocalDate.now().minusDays(1));

        mockMvc.perform(MockMvcRequestBuilders.put(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithNullId)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при id = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.put(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithNotFoundId)))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertInstanceOf(NotFoundException.class, result.getResolvedException(), "Тип ошибки не совпадает при не найденном id"));
    }

    @Test
    void updateWithEmailValidationException() throws Exception {
        User userWithNullEmail = new User(null, "User Name", "userLogin", null, LocalDate.now().minusDays(1));
        User userWithEmptyEmail = new User(null, "User Name", "userLogin", "", LocalDate.now().minusDays(1));
        User userWithWrongEmail = new User(null, "User Name", "userLogin", "some-email.ru", LocalDate.now().minusDays(1));

        mockMvc.perform(MockMvcRequestBuilders.put(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithNullEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при email = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.put(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithEmptyEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при пустом email"));

        mockMvc.perform(MockMvcRequestBuilders.put(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithWrongEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при email без @"));
    }

    @Test
    void updateWithLoginValidationException() throws Exception {
        User userWithNullLogin = new User(null, "User Name", null, "email@mail.ru", LocalDate.now().minusDays(1));
        User userWithEmptyLogin = new User(null, "User Name", "", "email@mail.ru", LocalDate.now().minusDays(1));
        User userWithSpacesLogin = new User(null, "User Name", "  ", "email@mail.ru", LocalDate.now().minusDays(1));

        mockMvc.perform(MockMvcRequestBuilders.put(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithNullLogin)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при логине = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.put(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithEmptyLogin)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при пустом логине"));

        mockMvc.perform(MockMvcRequestBuilders.put(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithSpacesLogin)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при логине с пустыми пробелами"));
    }

    @Test
    void updateWithBirthdayValidationException() throws Exception {
        User userWithNullBirthday = new User(null, "User name", "userLogin", "email@mail.ru", null);
        User userWithBirthdayInFuture = new User(null, "User name", "userLogin", "email@mail.ru", LocalDate.now().plusYears(1));

        mockMvc.perform(MockMvcRequestBuilders.put(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithNullBirthday)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при birthday = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.put(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithBirthdayInFuture)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при birthday в будущем"));
    }

}