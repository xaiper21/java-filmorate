package ru.yandex.practicum.filmorate.mapper;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.dtoclasses.GenreWithIdAndName;
import ru.yandex.practicum.filmorate.model.GenreWithId;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class GenreMapper {

    public static GenreWithId mapToGenreWithId(GenreWithIdAndName genreWithIdAndName) {
        GenreWithId genreWithId = new GenreWithId();
        genreWithId.setId(genreWithIdAndName.getId());
        return genreWithId;
    }

    public static List<GenreWithId> mapToListGenreWithId(List<GenreWithIdAndName> genreWithIdAndNames) {
        return genreWithIdAndNames.stream().map(GenreMapper::mapToGenreWithId).collect(Collectors.toList());
    }

    public static List<GenreWithId> mapToGenreWithId(List<GenreWithIdAndName> genreWithIdAndName) {
        return genreWithIdAndName.stream().map(GenreMapper::mapToGenreWithId).collect(Collectors.toList());
    }
}
