package ru.yandex.practicum.filmorate.dal;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmRepositoryTest {

    static class FakeJdbcTemplate extends JdbcTemplate {
        List<String> executedQueries = new ArrayList<>();
        List<Object[]> executedParams = new ArrayList<>();

        @Override
        public int update(String sql, Object... args) {
            executedQueries.add(sql);
            executedParams.add(args);

            return 1;
        }

        @Override
        public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
            executedQueries.add(sql);
            executedParams.add(args);

            List<T> films = new ArrayList<>();
            try {
                films.add(rowMapper.mapRow(null, 0));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return films;
        }
    }

    static class FakeRowMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(java.sql.ResultSet rs, int rowNum) {
            Film film = new Film();
            film.setId(1L);
            film.setName("Fake Film");
            return film;
        }
    }

    @Test
    void delete_shouldCallUpdateWithCorrectQueryAndId() {
        FakeJdbcTemplate jdbcTemplate = new FakeJdbcTemplate();
        FakeRowMapper rowMapper = new FakeRowMapper();
        FilmRepository repo = new FilmRepository(jdbcTemplate, rowMapper);

        long filmId = 42L;
        repo.delete(filmId);

        assertEquals(1, jdbcTemplate.executedQueries.size());
        assertTrue(jdbcTemplate.executedQueries.get(0).contains("DELETE FROM film WHERE id=?"));

        Object[] params = jdbcTemplate.executedParams.get(0);
        assertEquals(filmId, params[0]);
    }

    @Test
    void findCommonFilmsByUsers_shouldReturnFilms() {
        FakeJdbcTemplate jdbcTemplate = new FakeJdbcTemplate();
        FakeRowMapper rowMapper = new FakeRowMapper();
        FilmRepository repo = new FilmRepository(jdbcTemplate, rowMapper);

        long userId = 1L;
        long friendId = 2L;

        Collection<Film> films = repo.findCommonFilmsByUsers(userId, friendId);

        assertNotNull(films);
        assertFalse(films.isEmpty());

        assertEquals(1, jdbcTemplate.executedQueries.size());
        String sql = jdbcTemplate.executedQueries.get(0);
        assertTrue(sql.contains("JOIN film_like fl1 ON"));
        assertTrue(sql.contains("JOIN film_like fl2 ON"));
    }
}
