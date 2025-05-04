package ru.yandex.practicum.filmorate.dal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Size
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private Integer duration;
    @NotNull
    private long mpaId;
    @NotNull
    private Set<Long> genresIds;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Long> likesIds;
}
