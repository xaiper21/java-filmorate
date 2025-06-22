package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreWithId;
import ru.yandex.practicum.filmorate.model.MpaWithId;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Scope("singleton")
@AllArgsConstructor
public class FilmService {
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;
    private final FilmRepository filmRepository;

    public FilmResponseDto createFilm(FilmCreateRequestDto createRequestDto) {
        log.trace("Сервисный метод создания фильма");
        createRequestDto.setGenres(checkAndRemoveDuplicate(createRequestDto.getGenres()));
        if (createRequestDto.getGenres() == null) createRequestDto.setGenres(new ArrayList<>());
        List<GenreWithIdAndName> genres = getFullGenresByGenresWithId(createRequestDto.getGenres());
        MpaWithIdAndName mpa = getFillMpaByMpaWithId(createRequestDto.getMpa());
        Film film = FilmMapper.mapToFilm(createRequestDto);
        film.setId(filmRepository.save(film));
        genreRepository.insertGenresFilm(film.getGenres(), film.getId());
        return FilmMapper.buildResponse(film, mpa, genres);
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
        request.setGenres(checkAndRemoveDuplicate(request.getGenres()));
        Film oldFilm = filmRepository.findOne(request.getId()).orElseThrow(() ->
                new NotFoundException(Film.class.getName(), request.getId()));
        MpaWithIdAndName resultMpa;
        if (request.hasMpa()) {
            resultMpa = getFillMpaByMpaWithId(request.getMpa());
        } else {
            resultMpa = getFillMpaByMpaWithId(oldFilm.getMpa());
        }

        List<GenreWithIdAndName> resulGenres;
        if (request.hasGenres()) {
            resulGenres = getFullGenresByGenresWithId(request.getGenres());
        } else {
            resulGenres = getFullGenresByGenresWithId(oldFilm.getGenres());
        }
        filmRepository.updateFilm(FilmMapper.updateFields(oldFilm, request, resultMpa, resulGenres));
        genreRepository.updateGenresFilm(oldFilm.getGenres(), oldFilm.getId());
        return FilmMapper.buildResponse(oldFilm, resultMpa, resulGenres);
    }

    public Collection<FilmResponseDto> findAllFilms() {
        log.trace("Сервисный метод получение фильмов");
        return filmRepository.findAll()
                .stream()
                .map(film ->
                        FilmMapper.buildResponse(
                                film,
                                mpaRepository.findOne(film.getMpa().getId()).get(),
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
                                mpaRepository.findOne(film.getMpa().getId()).get(),
                                genreRepository.findAllGenresFilm(film.getId())))
                .collect(Collectors.toList());
    }

    public Collection<GenreWithIdAndName> findAllGenres() {
        return genreRepository.findAllGenres();
    }

    public Collection<MpaWithIdAndName> findAllRatings() {
        return mpaRepository.findMany();
    }

    public GenreWithIdAndName findGenreById(Integer id) {
        return genreRepository.findGenreById(id)
                .orElseThrow(() -> new NotFoundException(GenreWithId.class.getName(), id));
    }

    public MpaWithIdAndName findRatingById(Integer id) {
        return mpaRepository.findOne(id).orElseThrow(
                () -> new NotFoundException(MpaWithId.class.getName(), id));
    }

    public Collection<FilmResponseDto> findFilmsByGenre(Integer genreId) {
        GenreWithIdAndName genreWithIdAndName = findGenreById(genreId);
        return filmRepository.getFilmsByGenre(genreId)
                .stream()
                .map(film -> FilmMapper.buildResponse(
                        film,
                        mpaRepository.findOne(film.getMpa().getId()).get(),
                        genreRepository.findAllGenresFilm(film.getId())))
                .collect(Collectors.toList());
    }

    public FilmResponseDto findFilmById(Integer id) {
        Film film = filmRepository.findOne(id).orElseThrow(() ->
                new NotFoundException(Film.class.getName(), id));
        return FilmMapper.buildResponse(film,
                mpaRepository.findOne(film.getMpa().getId()).get(),
                genreRepository.findAllGenresFilm(film.getId()));
    }

    private List<GenreWithId> checkAndRemoveDuplicate(List<GenreWithId> genre) {
        List<GenreWithId> result = new ArrayList<>();
        if (genre == null) return result;
        for (GenreWithId genreWithId : genre) {
            if (!result.contains(genreWithId)) result.add(genreWithId);
        }
        return result;
    }
}