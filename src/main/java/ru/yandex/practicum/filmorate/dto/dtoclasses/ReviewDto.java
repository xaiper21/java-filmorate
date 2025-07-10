package ru.yandex.practicum.filmorate.dto.dtoclasses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewDto {
    private Long reviewId;
    @NotBlank
    private String content;
    @NotNull
    private Boolean isPositive;
    @NotNull
    private Long userId;
    @NotNull
    private Long filmId;
    private Integer useful;
}