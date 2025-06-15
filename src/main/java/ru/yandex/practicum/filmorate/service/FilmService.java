package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotFoundLikeException;
import ru.yandex.practicum.filmorate.exception.NullObject;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

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
        film.setId(filmStorage.getNextId());
        filmStorage.addFilm(film);
        return film;
    }

    public Film update(Film film) throws NullObject, NotFoundException {
        log.trace("Сервисный метод обновление фильма");
        if (film == null) throw new NullObject(Film.class);
        Long filmId = film.getId();
        if (!filmStorage.containsKey(filmId)) throw new NotFoundException(Film.class.getName(), filmId);
        filmStorage.updateFilm(film);
        return film;
    }

    public Collection<Film> findAll() {
        log.trace("Сервисный метод получение фильмов");
        return filmStorage.findAll();
    }

    public Like likeFilm(long filmId, long userId) throws NotFoundException {
        log.trace("Сервисный метод добавления лайков");
        if (!userService.containsUser(userId)) throw new NotFoundException(User.class.getName(), userId);
        Film film = getFilmById(filmId);
        if (film.getLikes().containsKey(userId)) {
            return film.getLikes().get(userId);
        }
        Like newLike = new Like(userId);
        film.getLikes().put(userId, newLike);
        return newLike;
    }

    private Film getFilmById(long idFilm) throws NotFoundException {
        Film film = filmStorage.get(idFilm);
        if (film == null) throw new NotFoundException(Film.class.getName(), idFilm);
        return film;
    }

    public boolean deleteLikeFilm(long filmId, long userId) throws NotFoundLikeException {
        log.trace("Сервисный метод удаления лайков");
        Film film = getFilmById(filmId);
        if (!film.getLikes().containsKey(userId)) throw new NotFoundLikeException(filmId, userId);
        filmStorage.get(filmId).getLikes().remove(userId);
        return true;
    }

    public Collection<Film> getPopularFilms(int count) {
        log.trace("Сервисный метод получение популярных фильмов");
        return filmStorage.getTopFilms(count);
    }
}