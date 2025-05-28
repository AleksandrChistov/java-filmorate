package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.dto.DirectorDto;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@RestController
@RequestMapping(DirectorController.URL)
@RequiredArgsConstructor
@Validated
public class DirectorController {
    public static final String URL = "/directors";
    private final DirectorService directorService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DirectorDto> getAll() {
        return directorService.getAll();
    }

    @GetMapping("/{directorId}")
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto getById(@PathVariable @Positive Long directorId) {
        return directorService.getById(directorId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DirectorDto create(@RequestBody @Validated DirectorDto directorDto) {
        return directorService.create(directorDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto update(@RequestBody @Validated DirectorDto directorDto) {
        return directorService.update(directorDto);
    }

    @DeleteMapping("/{directorId}")
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable @Positive Long directorId) {
        directorService.remove(directorId);
    }
}
