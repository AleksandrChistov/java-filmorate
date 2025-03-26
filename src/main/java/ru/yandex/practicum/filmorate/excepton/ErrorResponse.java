package ru.yandex.practicum.filmorate.excepton;

import org.springframework.http.HttpStatus;

public record ErrorResponse(String error, HttpStatus status) {
}
