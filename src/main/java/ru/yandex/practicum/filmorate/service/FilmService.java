package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmMaxLikesComparator;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@Scope("singleton")
@AllArgsConstructor
public class FilmService {
    FilmStorage filmStorage;
    private final UserService userService;

    public Film create(Film film) {
        log.trace("добавление фильма");

        if (valid(film)) {
            film.setId(filmStorage.getNextId());
            filmStorage.addFilm(film);
        }
        return film;
    }

    public Film update(Film film) {
        log.trace("обновление фильма");
        if (film.getId() == null || !filmStorage.containsKey(film.getId())) {
            throw new DataFilmException("Id не указан или не найден");
        }
        if (valid(film)) {
            filmStorage.updateFilm(film);
        }
        return film;
    }

    public Collection<Film> findAll() {
        log.trace("получение фильмов");
        return filmStorage.findAll();
    }

    private boolean valid(Film film) {
        LocalDate startFilmDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(startFilmDate))
            throw new ValidationException("дата релиза — не раньше 28.12.1895");
        return true;
    }

    public Like likeFilm(long filmId, long userId) {
        if (!filmStorage.containsKey(filmId)) throw new DataFilmException("фильм не найден");
        if (!userService.containsUser(userId)) throw new DataFilmException("Пользователь не найден");
        Film film = filmStorage.get(filmId);
        if (film.getLikes().containsKey(userId)) {
            return film.getLikes().get(userId);
        }
        Like newLike = new Like(userId);
        film.getLikes().put(userId, newLike);
        return newLike;
    }

    public boolean deleteLikeFilm(long filmId, long userId) {
        if (!filmStorage.containsKey(filmId)) throw new DataFilmException("Указанный фильм не найден");
        if (!userService.containsUser(userId)) throw new DataFilmException("Неизвестный пользователь");
        if (!filmStorage.get(filmId).getLikes().containsKey(userId))
            throw new DataFilmException("Ваш лайк у указанного фильма не найден");
        filmStorage.get(filmId).getLikes().remove(userId);
        return true;
    }

    public Collection<Film> getPopularFilms(int count) {
        return findAll().stream()
                .sorted(new FilmMaxLikesComparator())
                .limit(count)
                .collect(Collectors.toList());
    }
}
