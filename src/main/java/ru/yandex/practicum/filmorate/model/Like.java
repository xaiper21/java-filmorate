package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "userId")
public class Like {
    private final Long userId;
    private final LocalDateTime timeSetLike = LocalDateTime.now();
}
