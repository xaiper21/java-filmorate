package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.DataFilmException;
import ru.yandex.practicum.filmorate.exception.DateNotValidException;
import ru.yandex.practicum.filmorate.exception.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.ResponseError;

@Slf4j
@ResponseBody
@ControllerAdvice
public class HandlerError {

    @ExceptionHandler(NotFoundUserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError checkNotFoundUser(NotFoundUserException e) {
        log.trace("Обработка NotFoundUserException");
        return new ResponseError(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseError checkAll(MethodArgumentNotValidException e) {
        log.trace("Обработка ошибки валидации");
        return new ResponseError("Ошибка валидации данных");
    }

    @ExceptionHandler(DataFilmException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError checkNoDataFilmException(DataFilmException e) {
        log.trace("Обработка DataFilmException");
        return new ResponseError(e.getMessage());
    }

    @ExceptionHandler(DateNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError checkDateNotValidException(DateNotValidException e) {
        return new ResponseError(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError checkAllException(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseError("Возникла какая-то ошибка");
    }
}
