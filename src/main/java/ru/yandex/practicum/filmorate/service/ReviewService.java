package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.ReviewLikeRepository;
import ru.yandex.practicum.filmorate.dal.ReviewRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.create.CreateReviewDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.ReviewDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.ReviewLike;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final EventService eventService;

    public ReviewDto createReview(CreateReviewDto createReviewDto) {
        log.info("Создание отзыва для фильма с ID {}", createReviewDto.getFilmId());
        checkUserExists(createReviewDto.getUserId());
        checkFilmExists(createReviewDto.getFilmId());

        Review review = ReviewMapper.toReviewFromDto(createReviewDto);
        review = reviewRepository.save(review);

        var exist = reviewRepository.findByUserId(createReviewDto.getUserId());

        log.info("Сохраняем отзыв в БД с параметрами reviewId= {}, content={}, isPositive={}, userId={}, filmId={}, useful={}",
                review.getReviewId(), review.getContent(), review.getIsPositive(), review.getUserId(), review.getFilmId());

        if (exist.size() == 1) {
            eventService.createEvent(createReviewDto.getUserId(), review.getReviewId(), EventType.REVIEW, OperationType.ADD);
        }

        return ReviewMapper.toDto(review);
    }

//    public ReviewDto createReview(CreateReviewDto createReviewDto) {
//        log.info("Создание отзыва для фильма с ID {}", createReviewDto.getFilmId());
//        checkUserExists(createReviewDto.getUserId());
//        checkFilmExists(createReviewDto.getFilmId());
//
//        // Проверяем, есть ли уже отзыв от этого пользователя для этого фильма
//        Optional<Review> existingReviewOpt = reviewRepository.findByFilmIdAndUserId(createReviewDto.getFilmId(), createReviewDto.getUserId());
//
//        if (existingReviewOpt.isPresent()) {
//            // Можно выбросить исключение или вернуть ошибку
//            throw new IllegalStateException("Пользователь уже оставил отзыв для этого фильма");
//        }
//
//        Review review = ReviewMapper.toReviewFromDto(createReviewDto);
//        review = reviewRepository.save(review);
//        log.info("Сохраняем отзыв в БД с параметрами reviewId= {}, content={}, isPositive={}, userId={}, filmId={}, useful={}",
//                review.getReviewId(), review.getContent(), review.getIsPositive(), review.getUserId(), review.getFilmId());
//        eventService.createEvent(createReviewDto.getUserId(), review.getReviewId(), EventType.REVIEW, OperationType.ADD);
//        return ReviewMapper.toDto(review);
//    }

    public ReviewDto updateReview(ReviewDto reviewDto) {
        Review existingReview = getReviewOrThrow(reviewDto.getReviewId());

        existingReview.setContent(reviewDto.getContent());
        existingReview.setIsPositive(reviewDto.getIsPositive());

        reviewRepository.update(existingReview);
        eventService.createEvent(existingReview.getUserId(), reviewDto.getReviewId(), EventType.REVIEW, OperationType.UPDATE);
        return ReviewMapper.toDto(existingReview);
    }

    public void deleteReview(long id) {
        log.info("Удаление отзыва с ID {}", id);
        getReviewOrThrow(id);
        eventService.createEvent(getReviewById(id).getUserId(), id, EventType.REVIEW, OperationType.REMOVE);
        log.info("deleteReview userId = {}, reviewId={}, EventType.REVIEW, OperationType.ADD", getReviewById(id).getUserId(), id);
        reviewRepository.delete(id);
    }

    public ReviewDto getReviewById(long id) {
        log.info("Получение отзыва с ID {}", id);
        Review review = getReviewOrThrow(id);
        return ReviewMapper.toDto(review);
    }

    public List<ReviewDto> getReviewsForFilm(Long filmId, int count) {
        log.info("Получение {} отзывов для фильма с ID {}", count, filmId);
        if (filmId != null) {
            checkFilmExists(filmId);
            return reviewRepository.findByFilmId(filmId).stream()
                    .limit(count)
                    .map(ReviewMapper::toDto)
                    .collect(Collectors.toList());
        }
        return reviewRepository.findMany().stream()
                .limit(count)
                .map(ReviewMapper::toDto)
                .collect(Collectors.toList());
    }

    public void addLikeToReview(long reviewId, long userId, boolean isLike) {
        log.info("Добавление {} к отзыву с ID {} от пользователя с ID {}",
                isLike ? "лайка" : "дизлайка", reviewId, userId);
        getReviewOrThrow(reviewId);
        checkUserExists(userId);

        ReviewLike like = new ReviewLike();
        like.setReviewId(reviewId);
        like.setUserId(userId);
        like.setIsLike(isLike);

        reviewLikeRepository.addLike(like);
        updateReviewUsefulRating(reviewId);
        eventService.createEvent(userId, reviewId, EventType.REVIEW, OperationType.ADD);
        log.info("addLikeToReview userId = {}, reviewId={}, EventType.REVIEW, OperationType.ADD", userId, reviewId);
    }

    public void removeLikeFromReview(long reviewId, long userId) {
        log.info("Удаление реакции на отзыв с ID {} от пользователя с ID {}", reviewId, userId);
        getReviewOrThrow(reviewId);
        checkUserExists(userId);

        reviewLikeRepository.removeLike(reviewId, userId);
        updateReviewUsefulRating(reviewId);
        log.info("removeLikeFromReview userId = {}, reviewId={}, EventType.LIKE, OperationType.UPDATE", userId, reviewId);
        eventService.createEvent(userId, reviewId, EventType.LIKE, OperationType.UPDATE);
    }

    private Review getReviewOrThrow(long id) {
        Optional<Review> review = reviewRepository.findOne(id);
        if (review.isEmpty()) {
            throw new NotFoundException("Отзыв", id);
        }
        return review.get();
    }

    private void checkUserExists(long userId) {
        if (userRepository.findOne(userId).isEmpty()) {
            throw new NotFoundException("Пользователь", userId);
        }
    }

    private void checkFilmExists(long filmId) {
        if (filmRepository.findOne(filmId).isEmpty()) {
            throw new NotFoundException("Фильм", filmId);
        }
    }

    private void updateReviewUsefulRating(long reviewId) {
        int likesCount = reviewLikeRepository.countLikes(reviewId);
        int dislikesCount = reviewLikeRepository.countDislikes(reviewId);
        int usefulRating = likesCount - dislikesCount;

        reviewRepository.updateUsefulRating(reviewId, usefulRating);
    }
}