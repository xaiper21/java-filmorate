package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.dtoclasses.MpaWithIdAndName;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
@AllArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    public Collection<MpaWithIdAndName> findAllRatings() {
        log.trace("Метод получения всех mpa в RatingController");
        return ratingService.findAllRatings();
    }

    @GetMapping("/{id}")
    public MpaWithIdAndName findRatingById(@PathVariable Integer id) {
        log.trace("Метод получения рейтинга(mpa) по id = {} в RatingController", id);
        return ratingService.findRatingById(id);
    }
}
