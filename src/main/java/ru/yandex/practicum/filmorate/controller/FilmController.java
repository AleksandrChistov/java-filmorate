package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.dal.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping(FilmController.URL)
@RequiredArgsConstructor
@Validated
public class FilmController {
    private final FilmService filmService;

    public static final String URL = "/films";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseFilmDto> findAll() {
        return filmService.getAll();
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseFilmDto findById(@PathVariable @Positive Long filmId) {
        return filmService.getById(filmId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseFilmDto create(@Validated @RequestBody FilmDto newFilm) {
        return filmService.create(newFilm);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseFilmDto update(@Validated @RequestBody FilmDto newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable @Positive Long filmId, @PathVariable @Positive Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseFilmDto> findPopularFilms(@RequestParam(defaultValue = "10") int count,
                                                  @RequestParam(required = false) Long genreId,
                                                  @RequestParam(required = false) Long year) {
        return filmService.findPopularFilms(count, genreId, year);
    }

    @GetMapping("/common")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseFilmDto> findCommonFilms(
            @RequestParam @Positive Long userId,
            @RequestParam @Positive Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @DeleteMapping("/{filmId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean delete(@PathVariable @Positive Long filmId) {
        return filmService.delete(filmId);
    }

    @GetMapping("/director/{directorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseFilmDto> getAllFilmsDirector(@PathVariable @Positive Long directorId,
                                                 @RequestParam String sortBy) {
        return filmService.getFilmsDirectorById(directorId, sortBy);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseFilmDto> getFilmsFromSearch(@RequestParam(required = false) String query,
                                                    @RequestParam(required = false) String by) {
        return filmService.getFilmsFromSearch(query, by);
    }

}
