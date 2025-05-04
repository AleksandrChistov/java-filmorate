package ru.yandex.practicum.filmorate.dal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
