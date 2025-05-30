package ru.yandex.practicum.filmorate.exception;

public class NotFoundFilmException extends RuntimeException {
  public NotFoundFilmException(long id) {
    super("Фильм с id \"" + id + "\"  не найден");
  }
}