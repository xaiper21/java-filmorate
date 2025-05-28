package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.ResponseError;

@Slf4j
@ResponseBody
@ControllerAdvice
public class HandlerError {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError checkNotFoundUser(NotFoundUserException e) {
        log.error("перехват");
        return new ResponseError("NotFoundUserException");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseError checkAll(jakarta.validation.ValidationException e) {
        return new ResponseError("Jackarta exception");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError checkNoDataFilmException(DataFilmException e) {
        return new ResponseError("NotFoundFilmException");
    }
}
