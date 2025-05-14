package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.trace("добавление фильма");

        if (valid(film)) {
            film.setId(nextId());
            films.put(film.getId(), film);
        }
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.trace("обновление фильма");
        if (film.getId() == null || !films.containsKey(film.getId())) {
            throw new ValidationException("Id не указан или не найден");
        }
        Film oldFilm = films.get(film.getId());
        if (valid(film)) {
            oldFilm.setDuration(film.getDuration());
            oldFilm.setName(film.getName());
            oldFilm.setReleaseDate(film.getReleaseDate());
            oldFilm.setDuration(film.getDuration());
        }
        return film;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.trace("получение фильмов");
        return films.values().stream().collect(Collectors.toList());
    }

    private boolean valid(Film film) {
        LocalDate startFilmDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(startFilmDate))
            throw new ValidationException("дата релиза — не раньше 28.12.1895");
        return true;
    }

    private long nextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
