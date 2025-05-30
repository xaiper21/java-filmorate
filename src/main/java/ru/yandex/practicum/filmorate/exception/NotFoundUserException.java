package ru.yandex.practicum.filmorate.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(long id) {
        super("Пользователь с id \"" + id + "\" не найден");
    }
}