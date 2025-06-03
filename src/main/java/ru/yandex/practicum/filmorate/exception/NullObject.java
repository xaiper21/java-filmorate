package ru.yandex.practicum.filmorate.exception;

public class NullObject extends RuntimeException {
    public NullObject(Class name) {
        super(name.getName() + " не должен быть null");
    }
}
