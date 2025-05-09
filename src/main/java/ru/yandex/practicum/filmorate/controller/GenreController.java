package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping(GenreController.URL)
@RequiredArgsConstructor
@Validated
public class GenreController {
    private final GenreService genreService;

    public static final String URL = "/genres";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GenreDto> findAll() {
        return genreService.getAll();
    }

    @GetMapping("/{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDto findById(@PathVariable @Min(1) long genreId) {
        return genreService.getById(genreId);
    }
}
