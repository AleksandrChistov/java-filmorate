package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dto.GenreDto;
import ru.yandex.practicum.filmorate.dal.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(@Qualifier("genreDbStorage") GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public int addGenresByFilmId(Set<Long> genresIds, long filmId) {
        genresIds.forEach(this::getById);
        return genreStorage.addGenresByFilmId(genresIds, filmId);
    }

    public List<GenreDto> getAll() {
        return genreStorage.getAll().stream()
                .map(GenreMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }

    public GenreDto getById(long genreId) {
        return genreStorage.getById(genreId)
                .map(GenreMapper::mapToGenreDto)
                .orElseThrow(() -> new NotFoundException("Жанр с id = " + genreId + " не найден."));
    }

    public List<GenreDto> getAllByFilmId(long filmId) {
        return genreStorage.getAllByFilmId(filmId).stream()
                .map(GenreMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }
}
