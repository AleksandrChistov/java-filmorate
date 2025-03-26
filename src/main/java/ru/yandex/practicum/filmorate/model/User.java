package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    public Integer id;
    private String name;
    @NotBlank
    private String login;
    @NotBlank
    @Email
    private String email;
    private LocalDate birthday;
}
