package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EventRowMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        Event event = Event.builder().build();
        event.setEventId(rs.getInt("id"));
        event.setTimestamp(rs.getTimestamp("event_timestamp"));
        event.setUserId(rs.getInt("user_id"));
        event.setEventType(EventType.valueOf(rs.getString("event_type")));
        event.setOperation(OperationType.valueOf(rs.getString("operation_type")));
        event.setEntityId(rs.getInt("entity_id"));
        return event;
    }
}
