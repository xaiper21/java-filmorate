package ru.yandex.practicum.filmorate.mapper;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.create.CreateReviewDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.ReviewDto;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@NoArgsConstructor
public class ReviewMapper {
    public static ReviewDto toDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setReviewId(review.getReviewId());
        dto.setContent(review.getContent());
        dto.setIsPositive(review.getIsPositive());
        dto.setUserId(review.getUserId());
        dto.setFilmId(review.getFilmId());
        dto.setUseful(review.getUseful());
        return dto;
    }

    public static Review toReviewFromDto(CreateReviewDto dto) {
        Review review = new Review();
        review.setContent(dto.getContent());
        review.setIsPositive(dto.getIsPositive());
        review.setUserId(dto.getUserId());
        review.setFilmId(dto.getFilmId());
        review.setUseful(0);
        return review;
    }

    public static Review updateModel(Review review, ReviewDto reviewDto) {
        if (reviewDto.getContent() != null) {
            review.setContent(reviewDto.getContent());
        }
        return review;
    }

    public static List<ReviewDto> toDtoList(List<Review> reviews) {
        return reviews.stream().map(ReviewMapper::toDto).toList();
    }
}