package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.DirectorRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dto.dtoclasses.DirectorDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
public class DirectorRepository {
    private final JdbcTemplate jdbc;
    private final RowMapper<DirectorDto> mapper;

    private static final String FIND_ALL_QUERY = "SELECT id, name FROM directors";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM directors WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO directors (name) VALUES (?)";
    private static final String UPDATE_QUERY = "UPDATE directors SET name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM directors WHERE id = ?";
    private static final String FIND_DIRECTORS_BY_FILM_ID = "SELECT d.id, d.name FROM directors d " +
            "JOIN film_director fd ON d.id = fd.director_id WHERE fd.film_id = ?";
    private static final String FIND_FILMS_BY_DIRECTOR_SORTED_BY_LIKES = "SELECT f.id, f.name, f.description, " +
            "f.release_date, f.duration, f.rating_id, r.name AS rating_name " +
            "FROM film f " +
            "LEFT JOIN rating r ON f.rating_id = r.id " +
            "LEFT JOIN film_director fd ON f.id = fd.film_id " +
            "LEFT JOIN (SELECT film_id, COUNT(*) AS likes_count FROM film_like GROUP BY film_id) fl " +
            "ON f.id = fl.film_id " +
            "WHERE fd.director_id = ? " +
            "ORDER BY COALESCE(fl.likes_count, 0) DESC";
    private static final String FIND_FILMS_BY_DIRECTOR_SORTED_BY_YEAR = "SELECT f.id, f.name, f.description, " +
            "f.release_date, f.duration, f.rating_id, r.name AS rating_name " +
            "FROM film f " +
            "LEFT JOIN rating r ON f.rating_id = r.id " +
            "LEFT JOIN film_director fd ON f.id = fd.film_id " +
            "WHERE fd.director_id = ? " +
            "ORDER BY f.release_date";

    public DirectorRepository(JdbcTemplate jdbc, DirectorRowMapper mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public List<DirectorDto> findAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    public Optional<DirectorDto> findById(Long id) {
        return jdbc.query(FIND_BY_ID_QUERY, mapper, id).stream().findFirst();
    }

    public DirectorDto save(DirectorDto director) {
        jdbc.update(INSERT_QUERY, director.getName());
        return director;
    }

    public DirectorDto update(DirectorDto director) {
        jdbc.update(UPDATE_QUERY, director.getName(), director.getId());
        return director;
    }

    public void delete(Long id) {
        jdbc.update(DELETE_QUERY, id);
    }

    public List<DirectorDto> findDirectorsByFilmId(Long filmId) {
        return jdbc.query(FIND_DIRECTORS_BY_FILM_ID, mapper, filmId);
    }

    public List<Film> findFilmsByDirectorSorted(Long directorId, String sortBy) {
        String query = sortBy.equals("year") ? FIND_FILMS_BY_DIRECTOR_SORTED_BY_YEAR :
                FIND_FILMS_BY_DIRECTOR_SORTED_BY_LIKES;
        return jdbc.query(query, new FilmRowMapper(), directorId);
    }

    public void insertDirectorsForFilm(List<DirectorDto> directors, Long filmId) {
        String sql = "INSERT INTO film_director (film_id, director_id) VALUES (?, ?)";
        directors.forEach(director ->
                jdbc.update(sql, filmId, director.getId()));
    }

    public Map<Long, List<DirectorDto>> getAllFilmDirectors() {
        String sql = "SELECT fd.film_id, d.id, d.name FROM film_director fd " +
                "JOIN directors d ON fd.director_id = d.id";

        return jdbc.query(sql, rs -> {
            Map<Long, List<DirectorDto>> result = new HashMap<>();
            while (rs.next()) {
                Long filmId = rs.getLong("film_id");
                DirectorDto director = new DirectorDto();
                director.setId(rs.getLong("id"));
                director.setName(rs.getString("name"));
                result.computeIfAbsent(filmId, k -> new ArrayList<>()).add(director);
            }
            return result;
        });
    }
}