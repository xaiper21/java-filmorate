package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.dtoclasses.ReviewDto;
import ru.yandex.practicum.filmorate.dto.create.CreateReviewDto;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ReviewDto createReview(@Valid @RequestBody CreateReviewDto createReviewDto) {
        log.info("Добавление нового отзыва");
        return reviewService.createReview(createReviewDto);
    }

    @PutMapping
    public ReviewDto updateReview(@Valid @RequestBody ReviewDto reviewDto) {
        log.info("Обновление отзыва с ID {}", reviewDto.getReviewId());
        return reviewService.updateReview(reviewDto);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable long id) {
        log.info("{} - Удаление отзыва", id);
        reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    public ReviewDto getReviewById(@PathVariable long id) {
        log.info("{} - Получение отзыва по ID", id);
        return reviewService.getReviewById(id);
    }

    @GetMapping
    public List<ReviewDto> getReviews(@RequestParam(required = false) Long filmId, @RequestParam(defaultValue = "10") int count) {
        log.info("Получение отзывов");
        return reviewService.getReviewsForFilm(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToReview(@PathVariable long id, @PathVariable long userId) {
        log.info("PUT /reviews/{}/like/{} - Добавление лайка отзыву", id, userId);
        reviewService.addLikeToReview(id, userId, true);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislikeToReview(@PathVariable long id, @PathVariable long userId) {
        log.info("PUT /reviews/{}/dislike/{} - Добавление дизлайка отзыву", id, userId);
        reviewService.addLikeToReview(id, userId, false);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeFromReview(@PathVariable long id, @PathVariable long userId) {
        log.info("DELETE /reviews/{}/like/{} - Удаление лайка/дизлайка отзыву", id, userId);
        reviewService.removeLikeFromReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislikeFromReview(@PathVariable long id, @PathVariable long userId) {
        log.info("DELETE /reviews/{}/dislike/{} - Удаление дизлайка отзыву", id, userId);
        reviewService.removeLikeFromReview(id, userId);
    }
}