package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, long id) {
        super(message.substring(message.lastIndexOf(".") + 1)
                + " with id=" + id + " not found.");
    }
}
