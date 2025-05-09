package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.enums.RatingMPA;

@Data
@AllArgsConstructor
public class Mpa {
    @NotNull
    private Long id;
    @NotBlank
    private RatingMPA name;
}
