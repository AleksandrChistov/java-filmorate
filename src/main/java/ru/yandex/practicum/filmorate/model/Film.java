package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    public LocalDate releaseDate;
    @PositiveOrZero
    public Integer duration;
}
