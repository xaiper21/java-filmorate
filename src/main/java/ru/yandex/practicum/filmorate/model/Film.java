package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.dto.dtoclasses.GenreWithIdAndName;
import ru.yandex.practicum.filmorate.dto.dtoclasses.MpaWithIdAndName;

import java.time.LocalDate;
import java.util.List;

@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private MpaWithIdAndName mpa;
    private List<GenreWithId> genres;
}
