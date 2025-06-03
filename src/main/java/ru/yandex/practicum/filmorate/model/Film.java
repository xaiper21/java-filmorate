package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.annotation.OneOf;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private Long duration;
    @OneOf(isRegisterUp = true)
    @JsonProperty("rating")
    private String rating;
//    @NotNull
    private Collection<Genre> genres;

    private Map<Long, Like> likes = new HashMap<>();
}
