package ru.yandex.practicum.filmorate.dal;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.catsgram.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String INSERT_QUERY = "INSERT INTO film (name, description, release_date, duration," +
            " rating_id)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT film.id, film.name, film.description, film.release_date," +
            " film.duration, film.rating_id, r.name AS rating_name FROM film " +
            " LEFT JOIN rating r ON film.rating_id = r.id " +
            "WHERE film.id = ?";
    private static final String UPDATE_FILM_QUERY = "UPDATE film SET name=?, description=?, release_date=?, duration=?, " +
            "rating_id=? WHERE film.id=?";
    private static final String FIND_ALL_QUERY = "SELECT film.id, film.name, film.description, film.release_date," +
            " film.duration, rating_id, r.name AS rating_name FROM film " +
            " LEFT JOIN rating r ON film.rating_id = r.id ";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO film_like (user_id, film_id) VALUES (?, ?)";
    private static final String REMOVE_LIKE_QUERY = "DELETE FROM film_like WHERE user_id = ? AND film_id = ?";
    private static final String FIND_POPULAR_FILM_QUERY = "SELECT " +
            "    f.ID, " +
            "    f.NAME, " +
            "    f.DESCRIPTION, " +
            "    f.RELEASE_DATE, " +
            "    f.DURATION, " +
            "    f.RATING_ID, " +
            "    r.name AS rating_name " +
            "FROM " +
            "    FILM AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.id " +
            "LEFT JOIN " +
            "    (SELECT film_id, COUNT(film_id) AS like_count FROM film_like GROUP BY film_id) AS likes " +
            "    ON f.ID = likes.film_id " +
            "ORDER BY " +
            "    COALESCE(likes.like_count, 0) DESC " +
            "LIMIT ?";
    public static final String FIND_FILM_BY_GENRE_ID = "SELECT " +
            "    f.ID, " +
            "    f.NAME, " +
            "    f.DESCRIPTION, " +
            "    f.RELEASE_DATE, " +
            "    f.DURATION, " +
            "    f.RATING_ID " +
            "    r.name AS rating_name " +
            "FROM " +
            "    FILM AS f " +
            "    LEFT JOIN rating AS r ON f.rating_id = r.id " +
            "    LEFT JOIN FILM_GENRE AS FG ON f.ID = FG.FILM_ID " +
            "WHERE FG.GENRE_ID = ?";
    public static final String GET_ALL_FILM_GENRE = "SELECT film_id, genre_id FROM film_genre ORDER BY film_id";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public Long save(Film film) {
        return super.insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
    }

    public Optional<Film> findOne(long id) {
        return super.findOne(FIND_BY_ID_QUERY, id);
    }

    public void updateFilm(Film film) {
        super.update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
    }

    public List<Film> findAll() {
        return super.findMany(FIND_ALL_QUERY);
    }

    public void setInsertLikeQuery(Long userId, Long filmId) {
        jdbc.update(INSERT_LIKE_QUERY, userId, filmId);
    }

    public void removeLikeBy(Long userId, Long filmId) {
        super.delete(REMOVE_LIKE_QUERY, userId, filmId);
    }

    public Collection<Film> getPopularFilms(int count) {
        return super.findMany(FIND_POPULAR_FILM_QUERY, count);
    }

    public Collection<Film> getFilmsByGenre(Integer genreId) {
        return super.findMany(FIND_FILM_BY_GENRE_ID, genreId);
    }

    public Map<Long, List<Integer>> getAllFilmGenres() {
        return jdbc.query(GET_ALL_FILM_GENRE, rs -> {
            Map<Long, List<Integer>> result = new HashMap<>();
            while (rs.next()) {
                long filmId = rs.getLong("film_id");
                int genreId = rs.getInt("genre_id");
                result.computeIfAbsent(filmId, k -> new ArrayList<>()).add(genreId);
            }
            return result;
        });
    }
}
