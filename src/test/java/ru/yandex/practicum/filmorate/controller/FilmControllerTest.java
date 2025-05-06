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
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.dal.dto.GenreIdDto;
import ru.yandex.practicum.filmorate.dal.dto.MpaIdDto;
import ru.yandex.practicum.filmorate.dal.dto.NewFilmDto;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.validation.ReleaseDateValidator.MIN_RELEASE_DATE;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmRowMapper.class})
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createWithNameValidationException() throws Exception {
        Set<GenreIdDto> genreIdDtos = new HashSet<>();
        genreIdDtos.add(new GenreIdDto(1));
        NewFilmDto filmWithNullName = new NewFilmDto(null, "Film description", LocalDate.now(), 60, new MpaIdDto(1), genreIdDtos);
        NewFilmDto filmWithEmptyName = new NewFilmDto("", "Film description", LocalDate.now(), 60, new MpaIdDto(1), genreIdDtos);

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
        Set<GenreIdDto> genreIdDtos = new HashSet<>();
        genreIdDtos.add(new GenreIdDto(1));
        NewFilmDto filmWithNullDescription = new NewFilmDto("Film Name", null, LocalDate.now(), 60, new MpaIdDto(1), genreIdDtos);
        NewFilmDto filmWithEmptyDescription = new NewFilmDto("Film Name", "", LocalDate.now(), 60, new MpaIdDto(1), genreIdDtos);
        String chars201 = "Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description overall 201 ch";
        NewFilmDto filmWithMoreThan200CharsDescription = new NewFilmDto("Film Name", chars201, LocalDate.now(), 60, new MpaIdDto(1), genreIdDtos);

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
        Set<GenreIdDto> genreIdDtos = new HashSet<>();
        genreIdDtos.add(new GenreIdDto(1));
        NewFilmDto filmWithNullReleaseDate = new NewFilmDto("Film name", "Film description", null, 60, new MpaIdDto(1), genreIdDtos);
        NewFilmDto filmWithReleaseDateBeforeMinDate = new NewFilmDto("Film name", "Film description", MIN_RELEASE_DATE.minusDays(1), 60, new MpaIdDto(1), genreIdDtos);

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
        Set<GenreIdDto> genreIdDtos = new HashSet<>();
        genreIdDtos.add(new GenreIdDto(1));
        NewFilmDto filmWithNegativeDuration = new NewFilmDto("Film name", "Film description", LocalDate.now(), -1, new MpaIdDto(1), genreIdDtos);

        mockMvc.perform(MockMvcRequestBuilders.post(FilmController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNegativeDuration)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при duration меньше нуля"));
    }

    @Test
    void updateWithIdValidationException() throws Exception {
        Set<GenreIdDto> genreIdDtos = new HashSet<>();
        genreIdDtos.add(new GenreIdDto(1));
        FilmDto filmWithNotFoundId = new FilmDto(4L, "Film name", "Film description", LocalDate.now(), 60, new MpaIdDto(1), genreIdDtos);

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL + "/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNotFoundId)))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertInstanceOf(NotFoundException.class, result.getResolvedException(), "Тип ошибки не совпадает при не найденном id"));
    }

    @Test
    void updateWithNameValidationException() throws Exception {
        Set<GenreIdDto> genreIdDtos = new HashSet<>();
        genreIdDtos.add(new GenreIdDto(1));
        FilmDto filmWithNullName = new FilmDto(1L, null, "Film description", LocalDate.now(), 60, new MpaIdDto(1), genreIdDtos);
        FilmDto filmWithEmptyName = new FilmDto(1L, "", "Film description", LocalDate.now(), 60, new MpaIdDto(1), genreIdDtos);

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNullName)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при name = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithEmptyName)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при name = NULL"));
    }

    @Test
    void updateWithDescriptionValidationException() throws Exception {
        Set<GenreIdDto> genreIdDtos = new HashSet<>();
        genreIdDtos.add(new GenreIdDto(1));
        FilmDto filmWithNullDescription = new FilmDto(1L, "Film Name", null, LocalDate.now(), 60, new MpaIdDto(1), genreIdDtos);
        FilmDto filmWithEmptyDescription = new FilmDto(1L, "Film Name", "", LocalDate.now(), 60, new MpaIdDto(1), genreIdDtos);
        String chars201 = "Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description with overall 201 chars Description overall 201 ch";
        FilmDto filmWithMoreThan200CharsDescription = new FilmDto(1L, "Film Name", chars201, LocalDate.now(), 60, new MpaIdDto(1), genreIdDtos);

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNullDescription)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при description = NULL"));

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithEmptyDescription)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при пустом description"));

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithMoreThan200CharsDescription)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при description более 200 символов"));
    }

    @Test
    void updateWithReleaseDateValidationException() throws Exception {
        Set<GenreIdDto> genreIdDtos = new HashSet<>();
        genreIdDtos.add(new GenreIdDto(1));
        FilmDto filmWithNullReleaseDate = new FilmDto(1L, "Film name", "Film description", null, 60, new MpaIdDto(1), genreIdDtos);
        FilmDto filmWithReleaseDateBeforeMinDate = new FilmDto(1L, "Film name", "Film description", MIN_RELEASE_DATE.minusDays(1), 60, new MpaIdDto(1), genreIdDtos);

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNullReleaseDate)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки при releaseDate = NULL не совпадает"));

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithReleaseDateBeforeMinDate)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки при releaseDate после максимальной даты релиза не совпадает"));
    }

    @Test
    void updateWithDurationValidationException() throws Exception {
        Set<GenreIdDto> genreIdDtos = new HashSet<>();
        genreIdDtos.add(new GenreIdDto(1));
        FilmDto filmWithNegativeDuration = new FilmDto(1, "Film name", "Film description", LocalDate.now(), -1, new MpaIdDto(1L), genreIdDtos);

        mockMvc.perform(MockMvcRequestBuilders.put(FilmController.URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNegativeDuration)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException(), "Тип ошибки не совпадает при duration меньше нуля"));
    }

}