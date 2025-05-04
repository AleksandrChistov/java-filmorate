package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dal.dto.MpaDto;
import ru.yandex.practicum.filmorate.enums.RatingMPA;
import ru.yandex.practicum.filmorate.model.Mpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpaMapper {

    public static Mpa mapToMpa(MpaDto mpaDto) {
        return new Mpa(mpaDto.getId(), RatingMPA.fromTitle(mpaDto.getName()));
    }

    public static MpaDto mapToMpaDto(Mpa mpa) {
        return new MpaDto(mpa.getId(), mpa.getName().getTitle());
    }
}
