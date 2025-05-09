package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.dto.MpaDto;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping(MpaController.URL)
@RequiredArgsConstructor
@Validated
public class MpaController {
    private final MpaService mpaService;

    public static final String URL = "/mpa";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MpaDto> findAll() {
        return mpaService.getAll();
    }

    @GetMapping("/{mapId}")
    @ResponseStatus(HttpStatus.OK)
    public MpaDto findById(@PathVariable @Min(1) long mapId) {
        return mpaService.getById(mapId);
    }
}
