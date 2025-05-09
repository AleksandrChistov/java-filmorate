package ru.yandex.practicum.filmorate.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
@AllArgsConstructor
public class GenreDto {
    private final long id;
    private String name;
}
