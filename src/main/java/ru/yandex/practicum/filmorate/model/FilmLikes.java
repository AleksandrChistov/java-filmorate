package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FilmLikes {
    @NotNull
    private final long filmId;
    @NotNull
    private final long userId;
}
