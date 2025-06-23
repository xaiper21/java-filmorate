package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dto.dtoclasses.GenreWithIdAndName;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.GenreWithId;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreWithIdAndName findGenreById(Integer id) {
        log.trace("Получение жанра по id {} в GenreService", id);
        return genreRepository.findGenreById(id)
                .orElseThrow(() -> new NotFoundException(GenreWithId.class.getName(), id));
    }

    public Collection<GenreWithIdAndName> findAllGenres() {
        log.trace("Получение коллекции жанров в GenreService");
        return genreRepository.findAllGenres();
    }
}
