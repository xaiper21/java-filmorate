package ru.yandex.practicum.filmorate.dto.dtoclasses;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewLikeDto {
    @NotNull
    private Long reviewId;
    @NotNull
    private Long userId;
    @NotNull
    private Boolean isLike;
}