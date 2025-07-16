package ru.yandex.practicum.filmorate.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.mappers.DirectorRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dto.dtoclasses.DirectorDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DirectorRepository.class, DirectorRowMapper.class, FilmRowMapper.class})
public class DirectorRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DirectorRepository directorRepository;

    private DirectorDto testDirector;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM film_director");
        jdbcTemplate.execute("DELETE FROM directors");

        testDirector = new DirectorDto();
        testDirector.setName("Test Director");
    }

    @Test
    void findAll_ShouldReturnAllDirectors() {
        directorRepository.save(testDirector);

        DirectorDto anotherDirector = new DirectorDto();
        anotherDirector.setName("Another Director");
        directorRepository.save(anotherDirector);


        List<DirectorDto> directors = directorRepository.findAll();

        assertThat(directors).hasSize(2);
    }

    @Test
    void findById_ShouldReturnDirectorWhenExists() {
        DirectorDto savedDirector = directorRepository.save(testDirector);
        Optional<DirectorDto> foundDirector = directorRepository.findById(savedDirector.getId());

        assertThat(foundDirector).isPresent();
        assertThat(foundDirector.get().getName()).isEqualTo(testDirector.getName());
    }

    @Test
    void findById_ShouldReturnEmptyWhenNotExists() {
        Optional<DirectorDto> foundDirector = directorRepository.findById(999L);
        assertThat(foundDirector).isEmpty();
    }

    @Test
    void save_ShouldSaveDirectorAndReturnWithId() {
        DirectorDto savedDirector = directorRepository.save(testDirector);

        assertThat(savedDirector.getId()).isNotNull();

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM directors WHERE id = ?",
                Integer.class,
                savedDirector.getId()
        );
        assertThat(count).isEqualTo(1);
    }

    @Test
    void update_ShouldUpdateDirector() {
        DirectorDto savedDirector = directorRepository.save(testDirector);
        savedDirector.setName("Updated Name");
        directorRepository.update(savedDirector);

        String name = jdbcTemplate.queryForObject(
                "SELECT name FROM directors WHERE id = ?",
                String.class,
                savedDirector.getId()
        );
        assertThat(name).isEqualTo("Updated Name");
    }

    @Test
    void delete_ShouldRemoveDirector() {
        DirectorDto savedDirector = directorRepository.save(testDirector);
        directorRepository.delete(savedDirector.getId());

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM directors WHERE id = ?",
                Integer.class,
                savedDirector.getId()
        );
        assertThat(count).isEqualTo(0);
    }

}