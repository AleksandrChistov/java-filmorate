package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Friendship {
    @NotNull
    private final long userId;
    @NotNull
    private final long friendId;
}
