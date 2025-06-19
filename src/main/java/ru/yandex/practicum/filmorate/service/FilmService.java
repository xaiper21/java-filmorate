package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.RatingRepository;
import ru.yandex.practicum.filmorate.dto.create.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.GenreDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.RatingDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@Scope("singleton")
@AllArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final RatingRepository ratingRepository;
    private final UserService userService;

    public CreateFilmDto create(CreateFilmDto film) {
        log.trace("Сервисный метод добавление фильма");


        return filmRepository.save(film);
    }
//
//    public Film update(Film film) throws NullObject, NotFoundException {
//        log.trace("Сервисный метод обновление фильма");
//        if (film == null) throw new NullObject(Film.class);
//        Long filmId = film.getId();
//        if (!filmStorage.containsKey(filmId)) throw new NotFoundException(Film.class.getName(), filmId);
//        filmStorage.updateFilm(film);
//        return film;
//    }
//
    public Collection<Film> findAll() {
        log.trace("Сервисный метод получение фильмов");
        return filmRepository.findMany();
    }

//    public Like likeFilm(long filmId, long userId) throws NotFoundException {
//        log.trace("Сервисный метод добавления лайков");
//        if (!userService.containsUser(userId)) throw new NotFoundException(User.class.getName(), userId);
//        Film film = getFilmById(filmId);
//        if (film.getLikes().containsKey(userId)) {
//            return film.getLikes().get(userId);
//        }
//        Like newLike = new Like(userId);
//        film.getLikes().put(userId, newLike);
//        return newLike;
//    }

    private Film getFilmById(long idFilm) throws NotFoundException {
        Optional<Film> film = filmRepository.findOne(idFilm);
        if (film.isEmpty()) throw new NotFoundException(Film.class.getName(), idFilm);
        return film.get();
    }

//    public boolean deleteLikeFilm(long filmId, long userId) throws NotFoundLikeException {
//        log.trace("Сервисный метод удаления лайков");
//        Film film = getFilmById(filmId);
//        if (!film.getLikes().containsKey(userId)) throw new NotFoundLikeException(filmId, userId);
//        filmStorage.get(filmId).getLikes().remove(userId);
//        return true;
//    }

//    public Collection<Film> getPopularFilms(int count) {
//        log.trace("Сервисный метод получение популярных фильмов");
//        return filmStorage.getTopFilms(count);
//    }

    public Collection<GenreDto> findAllGenres() {
        return genreRepository.findMany();
    }

    public GenreDto findGenreById(Integer id) {
        Optional<GenreDto> genre = genreRepository.findOne(id);
        if (genre.isEmpty()) throw new NotFoundException(GenreDto.class.getName(), id);
        return genre.get();
    }

    public Collection<RatingDto> findAllRatings(){
        return ratingRepository.findMany();
    }

    public RatingDto findRatingById(Integer id){
        Optional<RatingDto> rating = ratingRepository.findOne(id);
        if (rating.isEmpty()) throw new NotFoundException(RatingDto.class.getName(), id);
        return rating.get();
    }
}