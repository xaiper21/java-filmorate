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

        List<GenreWithId> resultAdd = checkAndRemoveDuplicateAndContains(createRequestDto.getGenres(), allFullGenres);
        MpaWithIdAndName mpa = getFillMpaByMpaWithId(createRequestDto.getMpa());

        Film film = FilmMapper.mapToFilm(createRequestDto, mpa, resultAdd);
        film.setId(filmRepository.save(film));
        genreRepository.insertGenresFilm(resultAdd, film.getId());

        return FilmMapper.buildResponse(film, genFullGenresByList(resultAdd, allFullGenres));
    }

    private MpaWithIdAndName getFillMpaByMpaWithId(MpaWithId mpa) throws NotFoundException {
        return mpaRepository.findOne(mpa.getId()).orElseThrow(
                () -> new NotFoundException(MpaWithId.class.getName(), mpa.getId())
        );
    }

    public FilmResponseDto updateFilm(FilmUpdateDto request) throws NullObject, NotFoundException {
        log.trace("Сервисный метод обновление фильма");
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
            Map<Integer, String> allFullGenres = getMapGenres();
            request.setGenres(checkAndRemoveDuplicateAndContains(request.getGenres(), allFullGenres));
            resulGenres = genFullGenresByList(request.getGenres(), allFullGenres);
        } else {
            resulGenres = genreRepository.findAllGenresFilm(oldFilm.getId());

        }
        filmRepository.updateFilm(FilmMapper.updateFields(oldFilm, request, resultMpa, resulGenres));
        genreRepository.updateGenresFilm(GenreMapper.mapToListGenreWithId(resulGenres), oldFilm.getId());
        return FilmMapper.buildResponse(oldFilm, resulGenres);
    }

    public Collection<FilmResponseDto> findAllFilms() {
        log.trace("Сервисный метод получение фильмов");
        Map<Integer, String> allFullGenres = getMapGenres();
        List<Film> resultFilms = filmRepository.findAll();
        Map<Long, List<Integer>> mapFilmIdAndGenreIds = filmRepository.getAllFilmGenres();
        return resultFilms.stream()
                .map(film -> FilmMapper
                        .buildResponse(film,
                                genFullGenresByListIds(mapFilmIdAndGenreIds.get(film.getId()), allFullGenres)))
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

    public Collection<FilmResponseDto> getPopularFilms(int count, Integer genreId, Integer year) {
        log.trace("Сервисный метод получение популярных фильмов");
        Map<Integer, String> allFullGenres = getMapGenres();
        if (genreId != null && !allFullGenres.containsKey(genreId))
            throw new NotFoundException(GenreWithId.class.getName(), genreId);

        Map<Long, List<Integer>> mapFilmIdAndGenreIds = filmRepository.getAllFilmGenres();

        Collection<Film> result = filmRepository.getPopularFilms(count, genreId, year);
        return result.stream()
                .map(film -> FilmMapper
                        .buildResponse(film,
                                genFullGenresByListIds(mapFilmIdAndGenreIds.get(film.getId()), allFullGenres)))
                .collect(Collectors.toList());
    }

    public FilmResponseDto findFilmById(Integer id) {
        Film film = filmRepository.findOne(id).orElseThrow(() ->
                new NotFoundException(Film.class.getName(), id));
        return FilmMapper.buildResponse(film,
                genreRepository.findAllGenresFilm(film.getId()));
    }

    public void deleteFilm(Long id) {
        if (!filmRepository.delete(id)) {
            throw new NotFoundException(Film.class.getName(), id);
        }
    }

    private List<GenreWithId> checkAndRemoveDuplicateAndContains(List<GenreWithId> genre,
                                                                 Map<Integer, String> mapAllGenres) {
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

    private List<GenreWithIdAndName> genFullGenresByList(List<GenreWithId> genres, Map<Integer, String> mapAllGenres) {
        if (genres == null || genres.isEmpty()) return new ArrayList<>();
        return genres.stream()
                .map(GenreWithId::getId)
                .map(genreId -> new GenreWithIdAndName(genreId, mapAllGenres.get(genreId)))
                .collect(Collectors.toList());
    }

    private List<GenreWithIdAndName> genFullGenresByListIds(List<Integer> genreIds, Map<Integer, String> mapAllGenres) {
        if (genreIds == null || genreIds.isEmpty()) return new ArrayList<>();
        return genreIds.stream()
                .map(genreId -> new GenreWithIdAndName(genreId, mapAllGenres.get(genreId)))
                .collect(Collectors.toList());
    }

    public Collection<FilmResponseDto> findCommonFilms(long userId, long friendId) {
        Map<Integer, String> allFullGenres = getMapGenres();
        Map<Long, List<Integer>> mapFilmIdAndGenreIds = filmRepository.getAllFilmGenres();

        Collection<Film> commonFilms = filmRepository.findCommonFilmsByUsers(userId, friendId);

        return commonFilms.stream()
                .map(film -> FilmMapper.buildResponse(film,
                        genFullGenresByListIds(mapFilmIdAndGenreIds.get(film.getId()), allFullGenres)))
                .collect(Collectors.toList());
    }

}