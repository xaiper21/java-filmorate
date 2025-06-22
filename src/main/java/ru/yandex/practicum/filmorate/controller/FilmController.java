package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.create.FilmCreateRequestDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.LikeResponseDto;
import ru.yandex.practicum.filmorate.dto.update.FilmUpdateDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.GenreWithIdAndName;
import ru.yandex.practicum.filmorate.dto.dtoclasses.MpaWithIdAndName;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping
@Slf4j
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping("/films")
    public FilmResponseDto create(@Valid @NotNull @RequestBody FilmCreateRequestDto film) {
        log.trace("добавление фильма");
        return filmService.createFilm(film);
    }

    @PutMapping("/films")
    public FilmResponseDto update(@Valid @NotNull @RequestBody FilmUpdateDto film) {
        log.trace("обновление фильма");
        log.error(film.toString());
        return filmService.updateFilm(film);
    }

    @GetMapping("/films")
    public Collection<FilmResponseDto> findAll() {
        log.trace("получение фильмов");
        return filmService.findAllFilms();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public LikeResponseDto likeFilm(@PathVariable("id") long filmId, @PathVariable long userId) {
        log.trace("Добавление лайка фильму");
        return filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public boolean deleteLikeFilm(@PathVariable("id") long filmId, @PathVariable long userId) {
        log.trace("Удаление лайка");
        return filmService.deleteLikeFilm(filmId, userId);
    }

    @GetMapping("/films/popular")
    public Collection<FilmResponseDto> getPopularFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
        log.trace("Получить топ популярных фильмов");
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/genres")
    public Collection<GenreWithIdAndName> findAllGenres() {
        return filmService.findAllGenres();
    }

    @GetMapping("/genres/{id}")
    public GenreWithIdAndName findGenreById(@PathVariable Integer id) {
        return filmService.findGenreById(id);
    }

    @GetMapping("/mpa")
    public Collection<MpaWithIdAndName> findAllRatings() {
        return filmService.findAllRatings();
    }

    @GetMapping("/mpa/{id}")
    public MpaWithIdAndName findRatingById(@PathVariable Integer id) {
        return filmService.findRatingById(id);
    }

    @GetMapping("/films/{id}")
    public FilmResponseDto findFilmById(@PathVariable Integer id) {
        return filmService.findFilmById(id);
    }
}