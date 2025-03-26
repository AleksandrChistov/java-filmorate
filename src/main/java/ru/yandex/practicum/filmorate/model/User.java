package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    public Integer id;
    private String name;
    private String login;
    private String email;
    private LocalDate birthday;
}
