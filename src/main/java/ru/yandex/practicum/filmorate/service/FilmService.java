package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DateNotValidException;
import ru.yandex.practicum.filmorate.exception.NotFoundFilmException;
import ru.yandex.practicum.filmorate.exception.NotFoundLikeException;
import ru.yandex.practicum.filmorate.exception.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
@Scope("singleton")
@AllArgsConstructor
public class FilmService {
    FilmStorage filmStorage;
    private final UserService userService;

    public Film create(Film film) {
        log.trace("Сервисный метод добавление фильма");
        if (valid(film)) {
            film.setId(filmStorage.getNextId());
            filmStorage.addFilm(film);
        }
        return film;
    }

    public Film update(Film film) {
        log.trace("Сервисный метод обновление фильма");
        Long filmId = film.getId();
        if (film == null || !filmStorage.containsKey(filmId)) {
            throw new NotFoundFilmException(filmId);
        }
        if (valid(film)) {
            filmStorage.updateFilm(film);
        }
        return film;
    }

    public Collection<Film> findAll() {
        log.trace("Сервисный метод получение фильмов");
        return filmStorage.findAll();
    }

    private boolean valid(Film film) {
        LocalDate startFilmDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(startFilmDate))
            throw new DateNotValidException("Дата релиза — не раньше 28.12.1895");
        return true;
    }

    public Like likeFilm(long filmId, long userId) {
        log.trace("Сервисный метод добавления лайков");
        if (!userService.containsUser(userId)) throw new NotFoundUserException(userId);
        Film film = getFilmById(filmId);
        if (film.getLikes().containsKey(userId)) {
            return film.getLikes().get(userId);
        }
        Like newLike = new Like(userId);
        film.getLikes().put(userId, newLike);
        return newLike;
    }

    private Film getFilmById(long idFilm) throws NotFoundFilmException {
        Film film = filmStorage.get(idFilm);
        if (film == null) throw new NotFoundFilmException(idFilm);
        return film;
    }

    public boolean deleteLikeFilm(long filmId, long userId) {
        log.trace("Сервисный метод удаления лайков");
        Film film = getFilmById(filmId);
        if (!film.getLikes().containsKey(userId))
            throw new NotFoundLikeException(filmId, userId);
        filmStorage.get(filmId).getLikes().remove(userId);
        return true;
    }

    public Collection<Film> getPopularFilms(int count) {
        log.trace("Сервисный метод получение популярных фильмов");
        return filmStorage.getTopFilms(count);
    }
}