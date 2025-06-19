package ru.yandex.practicum.filmorate.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.DateNoEarlierThan;
import ru.yandex.practicum.filmorate.annotation.OneOf;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateFilmRequest {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @DateNoEarlierThan(date = "1895.12.28", format = "yyyy.MM.dd")
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private Long duration;
    @OneOf(values = {"G", "PG", "PG-13", "R", "NC"})
    private String rating;
    private List<String> genres;
}
