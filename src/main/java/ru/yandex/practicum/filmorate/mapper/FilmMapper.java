package ru.yandex.practicum.filmorate.mapper;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.dtoclasses.GenreWithIdAndName;
import ru.yandex.practicum.filmorate.dto.dtoclasses.MpaWithIdAndName;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dto.create.FilmCreateRequestDto;
import ru.yandex.practicum.filmorate.dto.update.FilmUpdateDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.FilmResponseDto;

import java.util.List;

@NoArgsConstructor
public class FilmMapper {
    public static FilmResponseDto buildResponse(Film film,
                                                MpaWithIdAndName mpa,
                                                List<GenreWithIdAndName> genres) {
        FilmResponseDto result = new FilmResponseDto();
        result.setId(film.getId());
        result.setName(film.getName());
        result.setDescription(film.getDescription());
        result.setDuration(film.getDuration());
        result.setReleaseDate(film.getReleaseDate());
        result.setGenres(genres);
        result.setMpa(mpa);
        return result;
    }

    public static Film mapToFilm(FilmCreateRequestDto filmCreateRequestDto) {
        Film film = new Film();
        film.setName(filmCreateRequestDto.getName());
        film.setReleaseDate(filmCreateRequestDto.getReleaseDate());
        film.setDescription(filmCreateRequestDto.getDescription());
        film.setDuration(filmCreateRequestDto.getDuration());
        film.setGenres(filmCreateRequestDto.getGenres());
        film.setMpa(filmCreateRequestDto.getMpa());
        return film;
    }

    public static Film updateFields(Film film,
                                    FilmUpdateDto request,
                                    MpaWithIdAndName mpa,
                                    List<GenreWithIdAndName> genres) {
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setDuration(request.getDuration());
        film.setReleaseDate(request.getReleaseDate());
        film.setMpa(MpaMapper.mapToMpaWithId(mpa));
        film.setGenres(GenreMapper.mapToListGenreWithId(genres));
        return film;
    }
}
