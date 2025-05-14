package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    Long id;

    @NotBlank
    String name;

    @Size(max = 200)
    String description;
    LocalDate releaseDate;

    @Positive
    Long duration;

    @JsonCreator
    public Film(@JsonProperty("name") String name,
                  @JsonProperty("description") String description,
                @JsonProperty("releaseDate") LocalDate releaseDate,
                @JsonProperty("duration") String duration) {

        this.releaseDate = releaseDate;
        this.description = description;
        this.name = name;
        try {
            Integer parseDuration = Integer.parseInt(duration);
            this.duration = Duration.ofMinutes(parseDuration).toMinutes();
        } catch (NumberFormatException e) {
            this.duration = Duration.parse(duration).toMinutes();
        }
    }
}
