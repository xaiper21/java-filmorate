package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.create.FilmCreateRequestDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.LikeResponseDto;
import ru.yandex.practicum.filmorate.dto.update.FilmUpdateDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public FilmResponseDto create(@Valid @NotNull @RequestBody FilmCreateRequestDto film) {
        log.trace("добавление фильма");
        return filmService.createFilm(film);
    }

    @PutMapping
    public FilmResponseDto update(@Valid @NotNull @RequestBody FilmUpdateDto film) {
        log.trace("обновление фильма");
        return filmService.updateFilm(film);
    }

    @GetMapping
    public Collection<FilmResponseDto> findAll() {
        log.trace("получение фильмов");
        return filmService.findAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public LikeResponseDto likeFilm(@PathVariable("id") long filmId, @PathVariable long userId) {
        log.trace("Добавление лайка фильму");
        return filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean deleteLikeFilm(@PathVariable("id") long filmId, @PathVariable long userId) {
        log.trace("Удаление лайка");
        return filmService.deleteLikeFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<FilmResponseDto> getPopularFilms(@Positive @RequestParam(name = "count", defaultValue = "10") int count,
                                                       @Positive @RequestParam(required = false) Integer genreId,
                                                       @Positive @RequestParam(required = false) Integer year) {
        log.trace("Получить топ популярных фильмов, Year = {}, GenreId = {}, Count = {}", year, genreId, count);
        return filmService.getPopularFilms(count, genreId, year);
    }

    @GetMapping("/{id}")
    public FilmResponseDto findFilmById(@PathVariable Integer id) {
        return filmService.findFilmById(id);
    }

    @GetMapping("/director/{directorId}")
    public List<FilmResponseDto> getFilmsByDirector(@PathVariable Long directorId, @RequestParam(required = false, defaultValue = "likes") String sortBy) {
        return filmService.getFilmsByDirector(directorId, sortBy);
    }

    @DeleteMapping("/{filmId}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Long filmId) {
        log.trace("Контроллер удаления фильма с id {}", filmId);
        filmService.deleteFilm(filmId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/common")
    public Collection<FilmResponseDto> getCommonFilms(
            @RequestParam("userId") long userId,
            @RequestParam("friendId") long friendId) {
        log.info("Получение общих фильмов пользователей {} и {}", userId, friendId);
        return filmService.findCommonFilms(userId, friendId);
    }

    @GetMapping("/search")
    public Collection<FilmResponseDto> searchFilms(
            @RequestParam String query,
            @RequestParam List<String> by
    ) {
        log.info("Поиск фильма с параметрами query = {} , by = {}", query, by);
        return filmService.searchFilms(query, by);
    }
}