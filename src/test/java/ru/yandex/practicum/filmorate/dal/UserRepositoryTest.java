package ru.yandex.practicum.filmorate.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class UserRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private RowMapper<User> userRowMapper;

    @InjectMocks
    private UserRepository userRepository;

    private final long existingUserId = 1L;
    private final long nonExistingUserId = 2L;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRepository = spy(new UserRepository(jdbcTemplate, userRowMapper));
        user = new User();
        user.setId(existingUserId);
    }

    @Test
    void deleteUser_userNotFound_returnsFalse() {
        doReturn(Optional.empty()).when(userRepository).findOne(nonExistingUserId);
        boolean result = userRepository.deleteUser(nonExistingUserId);

        assertFalse(result);
        verify(jdbcTemplate, never()).update(anyString(), anyLong());
    }

    @Test
    void deleteUser_userFound_deleteSuccessful_returnsTrue() {
        doReturn(Optional.of(user)).when(userRepository).findOne(existingUserId);
        when(jdbcTemplate.update(anyString(), eq(existingUserId))).thenReturn(1);
        boolean result = userRepository.deleteUser(existingUserId);

        assertTrue(result);
        verify(jdbcTemplate).update(anyString(), eq(existingUserId));
    }

}