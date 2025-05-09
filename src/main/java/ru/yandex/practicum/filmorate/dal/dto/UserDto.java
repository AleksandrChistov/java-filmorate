package ru.yandex.practicum.filmorate.dal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class UserDto {
    @NotNull
    public Long id;
    private String name;
    @NotBlank
    private String login;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Past
    private LocalDate birthday;
}
