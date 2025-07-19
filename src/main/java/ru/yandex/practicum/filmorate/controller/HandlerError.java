package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@Slf4j
@ResponseBody
@ControllerAdvice
public class HandlerError {

    @ExceptionHandler({NotFoundException.class, NotFoundLikeException.class, NullObject.class,
            InternalServerException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse checkNotFound(Exception e) {
        log.trace("Обработка NotFoundException");
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, DateNotValidException.class,
            HandlerMethodValidationException.class})
    public ErrorResponse checkValidError(Exception e) {
        log.trace("Обработка ошибки валидации {}", e.getMessage());
        return new ErrorResponse("Ошибка валидации данных " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse checkAllException(Exception e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse("Возникла какая-то ошибка");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException e) {
        log.warn("Ошибка валидации: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
