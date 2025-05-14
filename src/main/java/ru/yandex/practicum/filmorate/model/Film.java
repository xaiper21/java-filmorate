package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.NonNull;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    Long id;
    @NonNull
    String name;
    String description;
    LocalDate releaseDate;
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
            if (parseDuration < 0) throw new ValidationException("Продолжительность фильма должна быть положительной");
            this.duration = Duration.ofMinutes(parseDuration).toMinutes();
        } catch (NumberFormatException e) {
            this.duration = Duration.parse(duration).toMinutes();
        }
    }
}
