package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.dal.dto.NewUserDto;
import ru.yandex.practicum.filmorate.dal.dto.UserDto;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.dal.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class})
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createWithEmailValidationException() throws Exception {
        NewUserDto userWithNullEmail = new NewUserDto("User Name", "userLogin", null, LocalDate.now().minusDays(1));
        NewUserDto userWithEmptyEmail = new NewUserDto("User Name", "userLogin", "", LocalDate.now().minusDays(1));
        NewUserDto userWithWrongEmail = new NewUserDto("User Name", "userLogin", "some-email.ru", LocalDate.now().minusDays(1));

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
        NewUserDto userWithNullLogin = new NewUserDto("User Name", null, "email@mail.ru", LocalDate.now().minusDays(1));
        NewUserDto userWithEmptyLogin = new NewUserDto("User Name", "", "email@mail.ru", LocalDate.now().minusDays(1));
        NewUserDto userWithSpacesLogin = new NewUserDto("User Name", "  ", "email@mail.ru", LocalDate.now().minusDays(1));

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
        NewUserDto userWithNullBirthday = new NewUserDto("User name", "userLogin", "email@mail.ru", null);
        NewUserDto userWithBirthdayInFuture = new NewUserDto("User name", "userLogin", "email@mail.ru", LocalDate.now().plusYears(1));

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
        NewUserDto userWithNullName = new NewUserDto(null, "userLogin", "email@mail.ru", LocalDate.now().minusDays(1));
        NewUserDto userWithEmptyName = new NewUserDto("", "userLogin2", "email@mail.ru", LocalDate.now().minusDays(1));

        String createdWithNullNameBody = mockMvc.perform(MockMvcRequestBuilders.post(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithNullName)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String createdWithEmptyNameBody = mockMvc.perform(MockMvcRequestBuilders.post(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithEmptyName)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserDto createdWithNullName = objectMapper.readValue(createdWithNullNameBody, UserDto.class);
        UserDto createdWithEmptyName = objectMapper.readValue(createdWithEmptyNameBody, UserDto.class);

        assertEquals("userLogin", createdWithNullName.getName(), "Имя не совпадает с логином при имени = NULL");
        assertEquals("userLogin2", createdWithEmptyName.getName(), "Имя не совпадает с логином при пустом имени");
    }

    @Test
    void updateWithIdValidationException() throws Exception {
        UserDto userWithNotFoundId = new UserDto(7L, "User name", "userLogin7", "email@mail.ru", LocalDate.now().minusDays(1));

        mockMvc.perform(MockMvcRequestBuilders.put(UserController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithNotFoundId)))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertInstanceOf(NotFoundException.class, result.getResolvedException(), "Тип ошибки не совпадает при не найденном id"));
    }

    @Test
    void updateWithEmailValidationException() throws Exception {
        UserDto userWithNullEmail = new UserDto(null, "User Name", "userLogin", null, LocalDate.now().minusDays(1));
        UserDto userWithEmptyEmail = new UserDto(null, "User Name", "userLogin", "", LocalDate.now().minusDays(1));
        UserDto userWithWrongEmail = new UserDto(null, "User Name", "userLogin", "some-email.ru", LocalDate.now().minusDays(1));

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
        UserDto userWithNullLogin = new UserDto(null, "User Name", null, "email@mail.ru", LocalDate.now().minusDays(1));
        UserDto userWithEmptyLogin = new UserDto(null, "User Name", "", "email@mail.ru", LocalDate.now().minusDays(1));
        UserDto userWithSpacesLogin = new UserDto(null, "User Name", "  ", "email@mail.ru", LocalDate.now().minusDays(1));

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
        UserDto userWithNullBirthday = new UserDto(null, "User name", "userLogin", "email@mail.ru", null);
        UserDto userWithBirthdayInFuture = new UserDto(null, "User name", "userLogin", "email@mail.ru", LocalDate.now().plusYears(1));

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