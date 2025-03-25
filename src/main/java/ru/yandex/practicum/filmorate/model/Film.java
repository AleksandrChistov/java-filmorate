package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.Instant;

/**
 * Film.
 */
@Data
public class Film {
    public int id;
    private String name;
    private String description;
    public Instant releaseDate;
    public Duration duration;
}
