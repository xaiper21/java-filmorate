package ru.yandex.practicum.filmorate.exception;

public class DateNotValidException extends RuntimeException {
    public DateNotValidException(String message) {
        super(message);
    }
}