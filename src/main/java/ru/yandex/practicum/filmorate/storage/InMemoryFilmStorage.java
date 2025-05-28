package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();


    @Override
    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }

//    @Override
//    public void removeFilm(long id) {
//        films.remove(id);
//    }

    @Override
    public Film updateFilm(Film film) {
        Film oldFilm = films.get(film.getId());
        oldFilm.setDuration(film.getDuration());
        oldFilm.setName(film.getName());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setDuration(film.getDuration());
        return oldFilm;
    }

    @Override
    public boolean containsKey(long id) {
        return films.containsKey(id);
    }

    @Override
    public Film get(long id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> findAll() {
        return films.values().stream().collect(Collectors.toList());
    }

    @Override
    public long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
