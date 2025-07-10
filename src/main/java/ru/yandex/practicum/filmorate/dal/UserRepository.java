package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id=?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM users WHERE id=?";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET login=?, name=?, birthday=?, email=? WHERE id=?";
    private static final String FIND_ALL_FRIENDS_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id IN " +
            "(SELECT user_two_id FROM friendship WHERE user_one_id = ?)";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friendship (user_one_id, user_two_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND_BY_USER_ONE_ID_QUERY = "DELETE FROM friendship WHERE user_one_id = ? AND " +
            "user_two_id = ?";
    private static final String FIND_MUTUAL_FRIENDS_BY_ID_USERS = "SELECT USERS.ID, " +
            "       USERS.NAME, " +
            "       USERS.EMAIL, " +
            "       USERS.LOGIN, " +
            "       USERS.BIRTHDAY " +
            "FROM USERS " +
            "WHERE USERS.ID IN ( " +
            "    SELECT FRIENDSHIP.USER_TWO_ID FROM FRIENDSHIP " +
            "    WHERE USER_ONE_ID = ? AND  USER_TWO_ID IN ( " +
            "        SELECT FRIENDSHIP.USER_TWO_ID FROM FRIENDSHIP " +
            "        WHERE USER_ONE_ID = ? " +
            "        ))";


    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public Optional<User> findOne(long id) {
        return super.findOne(FIND_BY_ID_QUERY, id);
    }

    public List<User> findMany() {
        return super.findMany(FIND_ALL_QUERY);
    }

    public boolean delete(long id) {
        return super.delete(DELETE_BY_ID_QUERY, id);
    }

    public User save(User user) {
        long id = super.insert(INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        super.update(UPDATE_QUERY,
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getEmail(),
                user.getId()
        );
        return user;
    }

    public Collection<User> findManyFriendsByIdUser(long id) {
        return super.findMany(FIND_ALL_FRIENDS_USER_BY_ID_QUERY, id);
    }

    public void addFriend(long id, long friendId) {
        super.insert(INSERT_FRIEND_QUERY, id, friendId);
    }

    public void deleteFriend(long userOneId, long userTwoId) {
        super.delete(DELETE_FRIEND_BY_USER_ONE_ID_QUERY, userOneId, userTwoId);
    }

    public Collection<User> findAllMutualFriendsByIdUsers(Long userOneId, Long userTwoId) {
        return super.findMany(FIND_MUTUAL_FRIENDS_BY_ID_USERS, userOneId, userTwoId);
    }
}
