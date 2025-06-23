package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.dto.dtoclasses.MpaWithIdAndName;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreWithId;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepository.class, UserRowMapper.class, FilmRowMapper.class, FilmRepository.class})
class FilmorateApplicationTests {

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    @Test
    void contextLoads() {

    }

    @Test
    void testFindUserById() {

        User userdb = userRepository.save(createUser());

        Optional<User> userOptional = userRepository.findOne(userdb.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", userdb.getId())
                );
    }

    @Test
    void testEqualsData() {
        User user = createUser();
        User userdb = userRepository.save(user);
        assertEquals(user.getName(), userdb.getName());
        assertEquals(user.getEmail(), userdb.getEmail());
        assertEquals(user.getLogin(), userdb.getLogin());
        assertEquals(user.getBirthday(), userdb.getBirthday());

        User userdb2 = userRepository.findOne(userdb.getId()).get();
        assertEquals(user.getName(), userdb2.getName());
        assertEquals(user.getEmail(), userdb2.getEmail());
        assertEquals(user.getLogin(), userdb2.getLogin());
        assertEquals(user.getBirthday(), userdb2.getBirthday());
    }

    private User createUser() {
        User userDto = new User();
        userDto.setEmail("test@example.com");
        userDto.setName("Test Name");
        userDto.setLogin("test");
        userDto.setBirthday(LocalDate.of(1990, 1, 1));
        return userDto;
    }

    ;

    private User getUpdatedUser() {
        User userDto = new User();
        userDto.setEmail("test2@example.com");
        userDto.setName("Test Names");
        userDto.setLogin("tests");
        userDto.setBirthday(LocalDate.of(1991, 2, 2));
        return userDto;
    }

    @Test
    void testRemoveUser() {
        User userdb = userRepository.save(createUser());
        assertTrue(userRepository.findOne(userdb.getId()).isPresent());
        userRepository.delete(userdb.getId());
        assertTrue(userRepository.findOne(userdb.getId()).isEmpty());
    }

    @Test
    void testUpdateUser() {
        User userdb = userRepository.save(createUser());
        User userUpdate = getUpdatedUser();
        userUpdate.setId(userdb.getId());
        User updatedUser = userRepository.update(userUpdate);
        assertEquals(userUpdate, updatedUser);
    }

    @Test
    void testCreateFilm() {
        Film film = getTestFilm();
        long id = filmRepository.save(film);
        Optional<Film> filmOptional = filmRepository.findOne(id);
        assertTrue(filmOptional.isPresent());
    }

    Film getTestFilm() {
        GenreWithId genre = new GenreWithId();
        genre.setId(1);
        Film film = new Film();
        film.setId(1L);
        film.setName("test");
        film.setDescription("test description");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.now());
        film.setMpa(new MpaWithIdAndName(1, null));
        film.setGenres(List.of(genre));
        return film;
    }
}
