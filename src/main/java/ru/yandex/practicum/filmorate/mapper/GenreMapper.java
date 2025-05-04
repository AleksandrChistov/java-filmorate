package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dal.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenreMapper {

    public static Genre mapToGenre(GenreDto genreDto) {
        return new Genre(genreDto.getId(), genreDto.getName());
    }

    public static GenreDto mapToGenreDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
