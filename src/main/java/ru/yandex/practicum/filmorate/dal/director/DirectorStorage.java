package ru.yandex.practicum.filmorate.dal.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
    List<Director> getAll();

    Director add(Director director);

    Director update(Director director);

    void remove(Long id);

    Optional<Director> getById(Long id);

}
