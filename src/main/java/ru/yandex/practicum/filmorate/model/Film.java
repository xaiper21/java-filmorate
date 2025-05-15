package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    Long id;
    @NotBlank
    String name;
    @NotNull
    @Size(max = 200)
    String description;
    @NotNull
    LocalDate releaseDate;
    @NotNull
    @Positive
    Long duration;
}
