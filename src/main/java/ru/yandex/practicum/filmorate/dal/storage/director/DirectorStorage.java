package ru.yandex.practicum.filmorate.dal.storage.director;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface DirectorStorage {
    void addDirectorsByFilmId(Film film);

    List<Director> getAll();

    Director add(Director director);

    Director update(Director director);

    void remove(Long id);

    Optional<Director> getById(Long id);

    Map<Long, Set<Director>> getAllByFilmIds(List<Long> filmsIds);

    List<Director> getAllByFilmId(long filmId);
}
