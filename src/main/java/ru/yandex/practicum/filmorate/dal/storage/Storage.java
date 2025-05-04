package ru.yandex.practicum.filmorate.dal.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {
    List<T> getAll();

    Optional<T> getById(long id);

    T add(T entity);

    T update(T entity);

    boolean delete(long id);
}
