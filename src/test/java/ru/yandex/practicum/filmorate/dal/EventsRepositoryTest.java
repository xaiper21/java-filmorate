package ru.yandex.practicum.filmorate.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.EventRowMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({EventsRepository.class, EventRowMapper.class}) // Импортируем необходимые компоненты
public class EventsRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EventsRepository eventsRepository;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM events");

        testEvent = Event.builder()
                .eventId(1L)
                .timestamp(Timestamp.from(Instant.now()))
                .userId(1L)
                .eventType(EventType.LIKE)
                .operation(OperationType.ADD)
                .entityId(1L)
                .build();
    }

    // Класс для маппинга Event из ResultSet
    static class EventRowMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Event.builder()
                    .eventId(rs.getLong("event_id"))
                    .userId(rs.getLong("user_id"))
                    .entityId(rs.getLong("entity_id"))
                    .eventType(EventType.valueOf(rs.getString("event_type")))
                    .operation(OperationType.valueOf(rs.getString("operation")))
                    .timestamp(rs.getTimestamp("timestamp"))
                    .build();
        }
    }

    @Test
    void create_ShouldSaveEventAndReturnGeneratedId() {
        Long generatedId = eventsRepository.create(testEvent);

        assertThat(generatedId).isNotNull();

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM events WHERE id = ?",
                Integer.class,
                generatedId
        );
        assertThat(count).isEqualTo(1);
    }

    @Test
    void delete_ShouldRemoveEventAndReturnTrue() {
        Long eventId = eventsRepository.create(testEvent);
        boolean isDeleted = eventsRepository.delete(eventId);

        assertThat(isDeleted).isTrue();

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM events WHERE id = ?",
                Integer.class,
                eventId
        );
        assertThat(count).isEqualTo(0);
    }

    @Test
    void delete_ShouldReturnFalseIfEventNotFound() {
        boolean isDeleted = eventsRepository.delete(999L);
        assertThat(isDeleted).isFalse();
    }

    @Test
    void findAllByUserId_ShouldReturnEventsForUser() {
        eventsRepository.create(testEvent);

        Event anotherEvent = Event.builder()
                .eventId(2L)
                .timestamp(Timestamp.from(Instant.now()))
                .userId(2L)
                .eventType(EventType.LIKE)
                .operation(OperationType.ADD)
                .entityId(1L)
                .build();

        eventsRepository.create(anotherEvent);

        Collection<Event> events = eventsRepository.findAllByUserId(1L);

        assertThat(events).hasSize(1);
        assertThat(events.iterator().next().getUserId()).isEqualTo(1L);
    }

    @Test
    void findAll_ShouldReturnAllEvents() {
        eventsRepository.create(testEvent);
        eventsRepository.create(
                Event.builder()
                        .eventId(2L)
                        .timestamp(Timestamp.from(Instant.now()))
                        .userId(1L)
                        .eventType(EventType.LIKE)
                        .operation(OperationType.ADD)
                        .entityId(1L)
                        .build()
        );

        Collection<Event> events = eventsRepository.findAll();

        assertThat(events).hasSize(2);
    }
}