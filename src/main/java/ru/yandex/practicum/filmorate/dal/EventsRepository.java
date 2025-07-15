package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.Collection;

@Repository
public class EventsRepository extends BaseRepository<Event> {
    private static final String INSERT_QUERY = "" +
            "INSERT INTO events(" +
            "event_timestamp," +
            " user_id," +
            " event_type," +
            " operation_type," +
            " entity_id)" +
            " VALUES(?, ?, ?, ?, ?)";
    private static final String DELETE_BY_ID_QUERY = "DELETE events WHERE id = ?;";
    private static final String FIND_ALL_EVENTS_BY_USER_ID_QUERY = "SELECT * FROM events WHERE user_id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT * FROM events";

    public EventsRepository(JdbcTemplate jdbcTemplate, RowMapper<Event> mapper) {
        super(jdbcTemplate, mapper);
    }

    public Long create(Event event) {
        return super.insert(
                INSERT_QUERY,
                event.getTimestamp(),
                event.getUserId(),
                event.getEventType().name(),
                event.getOperation().name(),
                event.getEntityId());
    }

    public boolean delete(long id) {
        return delete(DELETE_BY_ID_QUERY, id);
    }

    public Collection<Event> findAllByUserId(long userId) {
        return super.findMany(FIND_ALL_EVENTS_BY_USER_ID_QUERY, userId);
    }

    public Collection<Event> findAll() {
        return super.findMany(FIND_ALL_QUERY);
    }
}
