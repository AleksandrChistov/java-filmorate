package ru.yandex.practicum.filmorate.mapper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dal.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.Director;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DirectorMapper {

    public static DirectorDto mapToDirectorDto(Director director) {
        return new DirectorDto(director.getId(), director.getName());
    }

    public static Director mapToDirector(DirectorDto directorDto) {
        return new Director(directorDto.getId(), directorDto.getName());
    }
}
