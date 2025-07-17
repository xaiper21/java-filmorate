package ru.yandex.practicum.filmorate.dto.dtoclasses;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FilmResponseDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private MpaWithIdAndName mpa;
    private List<GenreWithIdAndName> genres;
    private List<DirectorDto> directors;
}
