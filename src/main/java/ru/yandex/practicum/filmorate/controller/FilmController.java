package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.create.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.FilmDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.GenreDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.RatingDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public CreateFilmDto create(@Valid @RequestBody CreateFilmDto film) {
        log.trace("добавление фильма");
        return filmService.create(film);
    }
//
//    @PutMapping
//    public FilmDto update(@Valid @RequestBody Film film) {
//        log.trace("обновление фильма");
//        return filmService.update(film);
//    }
//
//    @GetMapping
//    public Collection<FilmDto> findAll() {
//        log.trace("получение фильмов");
//        return filmService.findAll();
//    }

//    @PutMapping("/{id}/like/{userId}")
//    public Like likeFilm(@PathVariable("id") long filmId, @PathVariable long userId) {
//        log.trace("Добавление лайка фильму");
//        return filmService.likeFilm(filmId, userId);
//    }

//    @DeleteMapping("/{id}/like/{userId}")
//    public boolean deleteLikeFilm(@PathVariable("id") long filmId, @PathVariable long userId) {
//        log.trace("Удаление лайка");
//        return filmService.deleteLikeFilm(filmId, userId);
//    }
//
//    @GetMapping("/popular")
//    public Collection<Film> getPopularFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
//        log.trace("Получить топ популярных фильмов");
//        return filmService.getPopularFilms(count);
//    }

    @GetMapping("/genres")
    public Collection<GenreDto> findAllGenres() {
        return filmService.findAllGenres();
    }

    @GetMapping("/genres/{id}")
    public GenreDto findGenreById(@PathVariable Integer id) {
        return filmService.findGenreById(id);
    }

    @GetMapping("/mpa")
    public Collection<RatingDto> findAllRatings(){
        return filmService.findAllRatings();
    }

    @GetMapping("/mpa/{id}")
    public RatingDto findRatingById(@PathVariable Integer id){
        return filmService.findRatingById(id);
    }
}