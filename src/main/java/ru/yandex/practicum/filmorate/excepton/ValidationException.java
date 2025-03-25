package ru.yandex.practicum.filmorate.excepton;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
