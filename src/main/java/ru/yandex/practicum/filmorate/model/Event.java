package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Event {
    private long eventId;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Timestamp timestamp;
    private long userId;
    private EventType eventType;
    private OperationType operation;
    private long entityId;
}
