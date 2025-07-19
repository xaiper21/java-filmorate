package ru.yandex.practicum.filmorate.dto.dtoclasses;

import lombok.Data;

@Data
public class LikeResponseDto {
    private String message = "Лайк успешно добавлен";
    private Long filmId;
    private Long userId;

    public LikeResponseDto(Long filmId, Long userId) {
        this.filmId = filmId;
        this.userId = userId;
    }
}