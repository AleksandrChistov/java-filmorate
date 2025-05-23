package ru.yandex.practicum.filmorate.dal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateReviewDto {
    @NotNull
    private Long reviewId;
    @NotBlank
    private String content;
    @NotNull
    private boolean isPositive;
    private Long userId;
    private Long filmId;
    private int useful;
}
