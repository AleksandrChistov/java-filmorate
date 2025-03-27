package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.Group;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@AllArgsConstructor
public class Film {
    @NotNull(groups = Group.OnUpdate.class)
    public Integer id;
    @NotBlank(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    private String name;
    @NotBlank(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    @Size(max = 200, groups = {Group.OnCreate.class, Group.OnUpdate.class})
    private String description;
    @ReleaseDate(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    public LocalDate releaseDate;
    @NotNull(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    @Positive(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    public Integer duration;
}
