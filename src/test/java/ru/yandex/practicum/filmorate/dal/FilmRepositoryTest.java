package ru.yandex.practicum.filmorate.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FilmRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private RowMapper<Film> filmRowMapper;

    @InjectMocks
    private FilmRepository filmRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filmRepository = spy(new FilmRepository(jdbcTemplate, filmRowMapper));
    }

    @Test
    void delete_shouldCallSuperDeleteWithCorrectQueryAndId() {
        long filmId = 42L;
        filmRepository.delete(filmId);
        verify(filmRepository).delete(eq(filmId));
        verify(filmRepository).delete(anyLong());

        verify(jdbcTemplate).update(eq("DELETE FROM film WHERE id=?"), eq(filmId));
    }

    @Test
    void findCommonFilmsByUsers() {
        long userId = 1L;
        long friendId = 2L;

        Film film1 = new Film();
        film1.setId(10L);
        film1.setName("Film 1");

        Film film2 = new Film();
        film2.setId(20L);
        film2.setName("Film 2");

        Collection<Film> expectedFilms = Arrays.asList(film1, film2);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong(), anyLong()))
                .thenReturn(Arrays.asList(film1, film2));

        Collection<Film> result = filmRepository.findCommonFilmsByUsers(userId, friendId);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<RowMapper<Film>> mapperCaptor = ArgumentCaptor.forClass(RowMapper.class);
        ArgumentCaptor<Long> userCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> friendCaptor = ArgumentCaptor.forClass(Long.class);

        verify(jdbcTemplate).query(sqlCaptor.capture(), mapperCaptor.capture(), userCaptor.capture(), friendCaptor.capture());

        String sql = sqlCaptor.getValue();
        RowMapper<Film> mapper = mapperCaptor.getValue();
        Long capturedUserId = userCaptor.getValue();
        Long capturedFriendId = friendCaptor.getValue();

        assertThat(sql).contains("JOIN film_like fl1 ON f.ID = fl1.film_id AND fl1.user_id = ?");
        assertThat(sql).contains("JOIN film_like fl2 ON f.ID = fl2.film_id AND fl2.user_id = ?");
        assertThat(capturedUserId).isEqualTo(userId);
        assertThat(capturedFriendId).isEqualTo(friendId);
        assertThat(mapper).isEqualTo(filmRowMapper);

        assertThat(result).isEqualTo(expectedFilms);
    }
}