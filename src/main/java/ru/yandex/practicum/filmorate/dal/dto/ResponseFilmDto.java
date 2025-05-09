package ru.yandex.practicum.filmorate.dal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import java.time.LocalDate;
import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseFilmDto {
    private long id;
    @NotBlank
    private final String name;
    @NotBlank
    @Size(max = 200)
    private final String description;
    @ReleaseDate
    private final LocalDate releaseDate;
    @NotNull
    @Positive
    private final Integer duration;
    @NotNull
    private final MpaDto mpa;
    @NotNull
    private final Set<GenreDto> genres;
    private final Set<Long> likes;
}
