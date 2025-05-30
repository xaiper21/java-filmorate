package ru.yandex.practicum.filmorate.exception;

public class NotFoundLikeException extends RuntimeException {
    public NotFoundLikeException(long userId, long filmId) {
        super("Лайк с id пользователя \"" + userId + "\"  и фильм id \"" + filmId + "\" не найден");
    }
}
