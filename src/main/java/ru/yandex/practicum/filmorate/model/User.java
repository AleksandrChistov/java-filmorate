package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.Group;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    @NotNull(groups = Group.OnUpdate.class)
    public Integer id;
    private String name;
    @NotBlank(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    private String login;
    @NotBlank(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    @Email(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    private String email;
    @NotNull(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    @Past(groups = {Group.OnCreate.class, Group.OnUpdate.class})
    private LocalDate birthday;
}
