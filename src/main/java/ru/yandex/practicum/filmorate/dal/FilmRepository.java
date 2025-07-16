package ru.yandex.practicum.filmorate.dal;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.DirectorRowMapper;
import ru.yandex.practicum.filmorate.dto.dtoclasses.DirectorDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
public class FilmRepository extends BaseRepository<Film> {
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
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM film WHERE id=?";
    private static final String FIND_MUTUAL_MOVIES_BY_ID_USERS_QUERY = "SELECT f.*, r.name AS rating_name\n" +
            "FROM film f\n" +
            "JOIN film_like fl ON f.id = fl.film_id\n" +
            "JOIN rating r ON f.rating_id = r.id\n" +
            "WHERE fl.user_id = (\n" +
            "    SELECT user_id\n" +
            "    FROM film_like\n" +
            "    WHERE film_id IN (SELECT film_id FROM film_like WHERE user_id = ?)\n" +
            "    AND user_id != ?\n" +
            "    GROUP BY user_id\n" +
            "    ORDER BY COUNT(*) DESC\n" +
            "    LIMIT 1\n" +
            ")\n" +
            "AND f.id NOT IN (\n" +
            "    SELECT film_id\n" +
            "    FROM film_like\n" +
            "    WHERE user_id = ?\n" +
            ")";

    private static final String FIND_COMMON_FILMS_BY_USERS_QUERY =
            "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, r.name AS rating_name " +
                    "FROM FILM f " +
                    "LEFT JOIN rating r ON f.rating_id = r.id " +
                    "JOIN film_like fl1 ON f.ID = fl1.film_id AND fl1.user_id = ? " +
                    "JOIN film_like fl2 ON f.ID = fl2.film_id AND fl2.user_id = ? " +
                    "LEFT JOIN (SELECT film_id, COUNT(user_id) AS like_count FROM film_like GROUP BY film_id) likes " +
                    "ON f.ID = likes.film_id " +
                    "ORDER BY COALESCE(likes.like_count, 0) DESC";
    // FilmRepository.java
    private static final String DELETE_DIRECTORS_FOR_FILM = "DELETE FROM film_director WHERE film_id = ?";

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

    public Collection<Film> getPopularFilms(int count, Integer genreId, Integer year) {
        List<Integer> params = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT " +
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
                "    ON f.ID = likes.film_id ");
        if (genreId != null) {
            query.append("LEFT JOIN film_genre AS fg ON f.id = fg.film_id ");
            query.append("WHERE fg.genre_id = ? ");
            params.add(genreId);
        }
        if (year != null) {
            if (genreId != null) {
                query.append(" AND EXTRACT(YEAR FROM f.RELEASE_DATE) = ? ");
            } else query.append(" WHERE EXTRACT(YEAR FROM f.RELEASE_DATE) = ? ");
            params.add(year);
        }
        query.append("ORDER BY " +
                "    COALESCE(likes.like_count, 0) DESC " +
                "LIMIT ?");
        params.add(count);
        return super.findMany(query.toString(), params.toArray());
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

    public Collection<Film> findCommonFilmsByUsers(long userId, long friendId) {
        return super.findMany(FIND_COMMON_FILMS_BY_USERS_QUERY, userId, friendId);
    }

    public Collection<Film> recommendationMovies(long userId) {
        return super.findMany(FIND_MUTUAL_MOVIES_BY_ID_USERS_QUERY, userId, userId, userId);
    }

    public boolean delete(long id) {
        return super.delete(DELETE_BY_ID_QUERY, id);
    }

    public void insertDirectorsForFilm(List<DirectorDto> directors, Long filmId) {
        if (directors == null || directors.isEmpty()) {
            return;
        }
        String sql = "INSERT INTO film_director (film_id, director_id) VALUES (?, ?)";
        directors.forEach(director -> jdbc.update(sql, filmId, director.getId()));
    }

    public void updateDirectorsForFilm(List<DirectorDto> directors, Long filmId) {
        jdbc.update(DELETE_DIRECTORS_FOR_FILM, filmId);
        insertDirectorsForFilm(directors, filmId);
    }

    public List<DirectorDto> findDirectorsByFilmId(Long filmId) {
        String sql = "SELECT d.id, d.name FROM directors d " +
                "JOIN film_director fd ON d.id = fd.director_id " +
                "WHERE fd.film_id = ?";
        return jdbc.query(sql, new DirectorRowMapper(), filmId);
    }
}
