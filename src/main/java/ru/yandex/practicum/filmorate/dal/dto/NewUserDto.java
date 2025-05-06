package ru.yandex.practicum.filmorate.dal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class NewUserDto {
    @NotBlank
    private String login;
    @NotBlank
    private String name;
    @Email
    private String email;
    @NotNull
    @Past
    private LocalDate birthday;
}
