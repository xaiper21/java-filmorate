package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.dtoclasses.GenreWithIdAndName;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public Collection<GenreWithIdAndName> findAllGenres() {
        log.trace("Метод получения всех жанров в GenreController");
        return genreService.findAllGenres();
    }

    @GetMapping("/{id}")
    public GenreWithIdAndName findGenreById(@PathVariable Integer id) {
        log.trace("Метод получения жанра по id= {} в GenreController", id);
        return genreService.findGenreById(id);
    }
}
