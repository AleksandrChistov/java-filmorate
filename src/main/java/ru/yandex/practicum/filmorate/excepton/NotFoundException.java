package ru.yandex.practicum.filmorate.excepton;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
