package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.catsgram.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dto.create.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.update.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM film";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM film WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO film (name, description, release_date, duration, rating_id)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_ID_RATING_BY_VALUE_QUERY = "SELECT id FROM rating WHERE name = ?";
    private static final String UPDATE_QUERY = "UPDATE film SET name=?, description=?, release_date=?, duration=?, +" +
            "rating_id WHERE id=?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Film> findOne(long id) {
        return super.findOne(FIND_BY_ID_QUERY, id);
    }

    public List<Film> findMany() {
        return super.findMany(FIND_ALL_QUERY);
    }

    public boolean delete(long id) {
        return super.delete(DELETE_BY_ID_QUERY, id);
    }

    public CreateFilmDto save(CreateFilmDto film) {
        long id = super.insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                getIdRatingByValue(film.getRating())
        );
        film.setId(id);
        return film;
    }

    public UpdateFilmRequest update(UpdateFilmRequest film) {
        super.update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                getIdRatingByValue(film.getRating()),
                film.getId());

        return film;
    }

    private int getIdRatingByValue(String value) {

        return jdbc.query(FIND_ID_RATING_BY_VALUE_QUERY, new Object[]{value}, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("id");
            }
        }).getFirst();
    }

    private void setGenres(List<String> genres) {

    }

    private void updateGenres(List<String> genres) {

    }
}
