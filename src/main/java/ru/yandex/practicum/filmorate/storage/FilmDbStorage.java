//package ru.yandex.practicum.filmorate.storage;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import ru.yandex.practicum.filmorate.model.Film;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//
//@Qualifier
//public class FilmDbStorage implements FilmStorage {
//    @Override
//    public void addFilm(Film film) {
//
//    }
//
//    @Override
//    public Film updateFilm(Film newFilm) {
//        return null;
//    }
//
//    @Override
//    public boolean containsKey(long id) {
//        return false;
//    }
//
//    @Override
//    public Optional<Film> get(long id) {
//        return null;
//    }
//
//    @Override
//    public Collection<Film> findAll() {
//        return List.of();
//    }
//
//    @Override
//    public long getNextId() {
//        return 0;
//    }
//
//    @Override
//    public Collection<Film> getTopFilms(int count) {
//        return List.of();
//    }
//}
