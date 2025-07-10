package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ReviewLike {
    @NotNull
    private Long reviewId;
    @NotNull
    private Long userId;
    @NotNull
    private Boolean isLike;
}