package ru.yandex.practicum.filmorate.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class UserRepositoryTest {

    static class FakeJdbcTemplate extends JdbcTemplate {
        String lastSql;
        Object[] lastParams;
        int updateResult = 0;

        @Override
        public int update(String sql, Object... args) {
            this.lastSql = sql;
            this.lastParams = args;
            return updateResult;
        }
    }

    static class FakeUserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(java.sql.ResultSet rs, int rowNum) {
            User user = new User();
            user.setId(1L);
            return user;
        }
    }

    private FakeJdbcTemplate jdbcTemplate;
    private FakeUserRowMapper userRowMapper;
    private UserRepository userRepository;

    private final long existingUserId = 1L;
    private final long nonExistingUserId = 2L;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new FakeJdbcTemplate();
        userRowMapper = new FakeUserRowMapper();
        userRepository = new UserRepository(jdbcTemplate, userRowMapper);
    }

    @Test
    void deleteUser_userNotFound_returnsFalse() {

        jdbcTemplate.updateResult = 0;

        boolean result = userRepository.deleteUser(nonExistingUserId);

        assertFalse(result);
        assertNotNull(jdbcTemplate.lastSql);
        assertTrue(jdbcTemplate.lastSql.toLowerCase().contains("delete"));
        assertEquals(nonExistingUserId, jdbcTemplate.lastParams[0]);
    }

    @Test
    void deleteUser_userFound_deleteSuccessful_returnsTrue() {

        UserRepository repoWithFindOne = new UserRepository(jdbcTemplate, userRowMapper) {
            @Override
            public Optional<User> findOne(long id) {
                if (id == existingUserId) {
                    User user = new User();
                    user.setId(existingUserId);
                    return Optional.of(user);
                }
                return Optional.empty();
            }
        };

        jdbcTemplate.updateResult = 1;

        boolean result = repoWithFindOne.deleteUser(existingUserId);

        assertTrue(result);
        assertNotNull(jdbcTemplate.lastSql);
        assertTrue(jdbcTemplate.lastSql.toLowerCase().contains("delete"));
        assertEquals(existingUserId, jdbcTemplate.lastParams[0]);
    }

}