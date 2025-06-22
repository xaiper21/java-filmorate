package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.catsgram.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dto.dtoclasses.GenreWithIdAndName;
import ru.yandex.practicum.filmorate.model.GenreWithId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<GenreWithIdAndName> {
    private static final String FIND_BY_ID_QUERY = "SELECT id AS genre_id, name AS genre_name FROM genre WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT id AS genre_id, name AS genre_name FROM genre ORDER BY id ASC";
    private static final String INSERT_GENRE_FILM_QUERY = "INSERT INTO film_genre SET genre_id = ?, film_id = ?";
    private static final String DELETE_GENRE_FILM_QUERY = "DELETE FROM film_genre WHERE film_id = ?";
    private static final String FIND_ALL_GENRES_FILM_BY_ID_QUERY = "SELECT " +
            "FILM_GENRE.GENRE_ID AS genre_id," +
            " G.NAME AS genre_name FROM FILM_GENRE " +
            "LEFT JOIN GENRE AS G on FILM_GENRE.GENRE_ID = G.ID " +
            "WHERE FILM_ID = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<GenreWithIdAndName> mapper) {
        super(jdbc, mapper);
    }

    public Optional<GenreWithIdAndName> findGenreById(Integer genreId) {
        return super.findOne(FIND_BY_ID_QUERY, genreId);
    }

    public Collection<GenreWithIdAndName> findAllGenres() {
        return super.findMany(FIND_ALL_QUERY);
    }

    public void insertGenreFilm(Integer genreId, Long filmId) {
        jdbc.update(INSERT_GENRE_FILM_QUERY, genreId, filmId);
    }

    public void insertGenresFilm(Collection<GenreWithId> genres, Long filmId) {
        genres.forEach(genre -> insertGenreFilm(genre.getId(), filmId));
    }

    public void deleteGenreFilm(Long filmId) {
        super.delete(DELETE_GENRE_FILM_QUERY, filmId);
    }

    public void updateGenresFilm(Collection<GenreWithId> genres, Long filmId) {
        deleteGenreFilm(filmId);
        insertGenresFilm(genres, filmId);
    }

    public List<GenreWithIdAndName> findAllGenresFilm(Long filmId) {
        return super.findMany(FIND_ALL_GENRES_FILM_BY_ID_QUERY, filmId);
    }
}
