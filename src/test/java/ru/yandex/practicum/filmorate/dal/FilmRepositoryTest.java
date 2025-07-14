//package ru.yandex.practicum.filmorate.dal;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
//import ru.yandex.practicum.filmorate.model.Film;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.verify;
//
//@JdbcTest
//@AutoConfigureTestDatabase
//@Import({FilmRepository.class, FilmRowMapper.class})
//class FilmRepositoryTest {
//
//    private JdbcTemplate jdbcTemplate;
//
//
//    private RowMapper<Film> filmRowMapper;
//
//
//    private FilmRepository filmRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        filmRepository = spy(new FilmRepository(jdbcTemplate, filmRowMapper));
//    }
//
//    @Test
//    void delete_shouldCallSuperDeleteWithCorrectQueryAndId() {
//        long filmId = 42L;
//        filmRepository.delete(filmId);
//        verify(filmRepository).delete(eq(filmId));
//        verify(filmRepository).delete(anyLong());
//
//        verify(jdbcTemplate).update(eq("DELETE FROM film WHERE id=?"), eq(filmId));
//    }
//}