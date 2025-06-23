package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.create.FilmCreateRequestDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.GenreWithIdAndName;
import ru.yandex.practicum.filmorate.dto.dtoclasses.LikeResponseDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.MpaWithIdAndName;
import ru.yandex.practicum.filmorate.dto.update.FilmUpdateDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullObject;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreWithId;
import ru.yandex.practicum.filmorate.model.MpaWithId;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;
    private final FilmRepository filmRepository;

    public FilmResponseDto createFilm(FilmCreateRequestDto createRequestDto) {
        log.trace("Сервисный метод создания фильма");
        Map<Integer, String> allFullGenres = getMapGenres();

        List<GenreWithId> resultAdd = checkAndRemoveDuplicateAndContains(createRequestDto.getGenres(),allFullGenres);
        MpaWithIdAndName mpa = getFillMpaByMpaWithId(createRequestDto.getMpa());

        Film film = FilmMapper.mapToFilm(createRequestDto,mpa,resultAdd);
        film.setId(filmRepository.save(film));
        genreRepository.insertGenresFilm(resultAdd, film.getId());

        List<GenreWithIdAndName> resultGenres = resultAdd.stream()
                .map(GenreWithId::getId)
                .map(id -> new GenreWithIdAndName(id,allFullGenres.get(id)))
                .collect(Collectors.toList());

        return FilmMapper.buildResponse(film, resultGenres);
    }

    private List<GenreWithIdAndName> getFullGenresByGenresWithId(List<GenreWithId> genres) throws NotFoundException {
        List<GenreWithIdAndName> result = new ArrayList<>();
        if (genres == null || genres.isEmpty()) return result;
        genres.forEach(genre -> {
            genreRepository.findGenreById(genre.getId())
                    .ifPresentOrElse(
                            result::add,
                            () -> {
                                throw new NotFoundException(GenreWithId.class.getName(), genre.getId());
                            });
        });
        return result;
    }

    private MpaWithIdAndName getFillMpaByMpaWithId(MpaWithId mpa) throws NotFoundException {
        return mpaRepository.findOne(mpa.getId()).orElseThrow(
                () -> new NotFoundException(MpaWithId.class.getName(), mpa.getId())
        );
    }

    public FilmResponseDto updateFilm(FilmUpdateDto request) throws NullObject, NotFoundException {
        log.trace("Сервисный метод обновление фильма");
        Map<Integer, String> allFullGenres = getMapGenres();

        request.setGenres(checkAndRemoveDuplicateAndContains(request.getGenres(),allFullGenres));
        Film oldFilm = filmRepository.findOne(request.getId()).orElseThrow(() ->
                new NotFoundException(Film.class.getName(), request.getId()));
        MpaWithIdAndName resultMpa;
        if (request.hasMpa()) {
            resultMpa = getFillMpaByMpaWithId(request.getMpa());
        } else {
            resultMpa = oldFilm.getMpa();
        }

        List<GenreWithIdAndName> resulGenres;
        if (request.hasGenres()) {
            resulGenres = request.getGenres().stream()
                    .map(GenreWithId::getId)
                    .map(id -> new GenreWithIdAndName(id,allFullGenres.get(id)))
                    .collect(Collectors.toList());
        } else {
                resulGenres = genreRepository.findAllGenresFilm(oldFilm.getId());

        }
        filmRepository.updateFilm(FilmMapper.updateFields(oldFilm, request, resultMpa, resulGenres));
        genreRepository.updateGenresFilm(GenreMapper.mapToListGenreWithId(resulGenres), oldFilm.getId());
        return FilmMapper.buildResponse(oldFilm, resulGenres);
    }

    public Collection<FilmResponseDto> findAllFilms() {
        log.trace("Сервисный метод получение фильмов");
        return filmRepository.findAll()
                .stream()
                .map(film ->
                        FilmMapper.buildResponse(
                                film,
                                genreRepository.findAllGenresFilm(film.getId())))
                .collect(Collectors.toList());
    }

    public LikeResponseDto likeFilm(long filmId, long userId) throws NotFoundException {
        log.trace("Сервисный метод добавления лайков");
        Film film = filmRepository.findOne(filmId).orElseThrow(() ->
                new NotFoundException(Film.class.getName(), filmId));
        Optional<User> optionalUser = userRepository.findOne(userId);
        if (optionalUser.isEmpty()) throw new NotFoundException(User.class.getName(), userId);

        filmRepository.setInsertLikeQuery(userId, filmId);
        return null;
    }

    public boolean deleteLikeFilm(long filmId, long userId) {
        log.trace("Сервисный метод удаления лайков");
        Film film = filmRepository.findOne(filmId).orElseThrow(() ->
                new NotFoundException(Film.class.getName(), filmId));

        Optional<User> optionalUser = userRepository.findOne(userId);
        if (optionalUser.isEmpty()) throw new NotFoundException(User.class.getName(), userId);
        filmRepository.removeLikeBy(userId, filmId);
        return true;
    }

    public Collection<FilmResponseDto> getPopularFilms(int count) {
        log.trace("Сервисный метод получение популярных фильмов");

        Collection<Film> result = filmRepository.getPopularFilms(count);
        return result.stream()
                .map(film ->
                        FilmMapper.buildResponse(
                                film,
                                genreRepository.findAllGenresFilm(film.getId())))
                .collect(Collectors.toList());
    }

    public FilmResponseDto findFilmById(Integer id) {
        Film film = filmRepository.findOne(id).orElseThrow(() ->
                new NotFoundException(Film.class.getName(), id));
        return FilmMapper.buildResponse(film,
                genreRepository.findAllGenresFilm(film.getId()));
    }

    private List<GenreWithId> checkAndRemoveDuplicateAndContains(List<GenreWithId> genre,
                                                                 Map<Integer,String> mapAllGenres) {
        List<GenreWithId> result = new ArrayList<>();
        if (genre == null) return result;
        for (GenreWithId genreWithId : genre) {
            if (!mapAllGenres.containsKey(genreWithId.getId())) throw new NotFoundException(
                    GenreWithId.class.getName(),
                    genreWithId.getId());
            if (!result.contains(genreWithId)) result.add(genreWithId);
        }
        return result;
    }

    private Map<Integer, String> getMapGenres() {
        return genreRepository.findAllGenres()
                .stream()
                .collect(Collectors
                        .toMap(GenreWithIdAndName::getId,
                                GenreWithIdAndName::getName)
                );
    }



}