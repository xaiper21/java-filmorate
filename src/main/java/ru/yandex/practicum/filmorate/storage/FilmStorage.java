package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    void addFilm(Film film);

    Film updateFilm(Film newFilm);

    boolean containsKey(long id);

    Film get(long id);

    Collection<Film> findAll();

    long getNextId();

    Collection<Film> getTopFilms(int count);
}
