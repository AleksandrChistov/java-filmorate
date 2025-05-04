package ru.yandex.practicum.filmorate.dal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenreDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank
    private String name;
}
