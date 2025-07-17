package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.EventsRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
@AllArgsConstructor
public class EventService {
    private final EventsRepository eventsRepository;

    public long createEvent(long userId, long entityId, EventType eventType, OperationType operationType) {
        Event event = Event
                .builder()
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .userId(userId)
                .eventType(eventType)
                .operation(operationType)
                .entityId(entityId)
                .build();
        return eventsRepository.create(event);
    }

    public Collection<Event> findAllByUserId(long userId) {
        Collection<Event> events = eventsRepository.findAllByUserId(userId);
        if (events.isEmpty()) {
            throw new NotFoundException("Событий у данного пользователя не найдено в базе данных. Пользователь - ", userId);
        }
        return events;
    }

    public Collection<Event> findAll() {
        return eventsRepository.findAll();
    }
}
