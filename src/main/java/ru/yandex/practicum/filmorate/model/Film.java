package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@AllArgsConstructor
public class Film {
    public Integer id;
    private String name;
    private String description;
    public LocalDate releaseDate;
    public Integer duration;
}
