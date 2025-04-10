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
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.validation.ReleaseDateValidator.MIN_RELEASE_DATE;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createWithNameValidationException() throws Exception {
        Film filmWithNullName = new Film(null, null, "Film description", LocalDate.now(), 60);
        Film filmWithEmptyName = new Film(null, "", "Film description", LocalDate.now(), 60);

        mockMvc.perform(MockMvcRequestBuilders.post(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNullName)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при name = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.post(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithEmptyName)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при name = NULL"));
    }

    @Test
    void createWithDescriptionValidationException() throws Exception {
        Film filmWithNullDescription = new Film(null, "Film Name", null, LocalDate.now(), 60);
        Film filmWithEmptyDescription = new Film(null, "Film Name", "", LocalDate.now(), 60);
        String chars201 = "Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description overall 201 ch";
        Film filmWithMoreThan200CharsDescription = new Film(null, "Film Name", chars201, LocalDate.now(), 60);

        mockMvc.perform(MockMvcRequestBuilders.post(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNullDescription)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при description = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.post(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithEmptyDescription)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при пустом description"));

        mockMvc.perform(MockMvcRequestBuilders.post(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithMoreThan200CharsDescription)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при description более 200 символов"));
    }

    @Test
    void createWithReleaseDateValidationException() throws Exception {
        Film filmWithNullReleaseDate = new Film(null, "Film name", "Film description", null, 60);
        Film filmWithReleaseDateBeforeMinDate = new Film(null, "Film name", "Film description", MIN_RELEASE_DATE.minusDays(1), 60);

        mockMvc.perform(MockMvcRequestBuilders.post(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNullReleaseDate)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки при releaseDate = NULL не совпадает"));

        mockMvc.perform(MockMvcRequestBuilders.post(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithReleaseDateBeforeMinDate)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки при releaseDate после максимальной даты релиза не совпадает"));
    }

    @Test
    void createWithDurationValidationException() throws Exception {
        Film filmWithNullDuration = new Film(null, "Film name", "Film description", LocalDate.now(), null);
        Film filmWithNegativeDuration = new Film(null, "Film name", "Film description", LocalDate.now(), -1);

        mockMvc.perform(MockMvcRequestBuilders.post(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNullDuration)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при duration = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.post(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNegativeDuration)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при duration меньше нуля"));
    }

    @Test
    void updateWithIdValidationException() throws Exception {
        Film filmWithNullId = new Film(null, "Film name", "Film description", LocalDate.now(), 60);
        Film filmWithNotFoundId = new Film(1, "Film name", "Film description", LocalDate.now(), 60);

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNullId)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при id = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNotFoundId)))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertInstanceOf(NotFoundException.class, result.getResolvedException(), "Тип ошибки не совпадает при не найденном id"));
    }

    @Test
    void updateWithNameValidationException() throws Exception {
        Film filmWithNullName = new Film(1, null, "Film description", LocalDate.now(), 60);
        Film filmWithEmptyName = new Film(1, "", "Film description", LocalDate.now(), 60);

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNullName)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при name = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithEmptyName)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при name = NULL"));
    }

    @Test
    void updateWithDescriptionValidationException() throws Exception {
        Film filmWithNullDescription = new Film(null, "Film Name", null, LocalDate.now(), 60);
        Film filmWithEmptyDescription = new Film(null, "Film Name", "", LocalDate.now(), 60);
        String chars201 = "Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description overall 201 ch";
        Film filmWithMoreThan200CharsDescription = new Film(null, "Film Name", chars201, LocalDate.now(), 60);

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNullDescription)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при description = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithEmptyDescription)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при пустом description"));

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithMoreThan200CharsDescription)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при description более 200 символов"));
    }

    @Test
    void updateWithReleaseDateValidationException() throws Exception {
        Film filmWithNullReleaseDate = new Film(null, "Film name", "Film description", null, 60);
        Film filmWithReleaseDateBeforeMinDate = new Film(null, "Film name", "Film description", MIN_RELEASE_DATE.minusDays(1), 60);

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNullReleaseDate)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки при releaseDate = NULL не совпадает"));

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithReleaseDateBeforeMinDate)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки при releaseDate после максимальной даты релиза не совпадает"));
    }

    @Test
    void updateWithDurationValidationException() throws Exception {
        Film filmWithNullDuration = new Film(null, "Film name", "Film description", LocalDate.now(), null);
        Film filmWithNegativeDuration = new Film(null, "Film name", "Film description", LocalDate.now(), -1);

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNullDuration)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при duration = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNegativeDuration)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при duration меньше нуля"));
    }

}