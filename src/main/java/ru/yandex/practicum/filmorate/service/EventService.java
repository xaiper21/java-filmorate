package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.EventsRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.OperationType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {
    private final EventsRepository eventsRepository;

    public long createEvent(long userId, long entityId, EventType eventType, OperationType operationType) {
        log.info("Создание Event userId= {}, entityId= {}, eventType= {}", userId, entityId, eventType);
        Event event = Event
                .builder()
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .userId(userId)
                .eventType(eventType)
                .operation(operationType)
                .entityId(entityId)
                .build();
        log.info("Получаем Event timestamp= {}, userId= {}, eventType= {}, operation= {}, entityId={}",
                event.getTimestamp(), event.getUserId(), event.getEventType(), event.getOperation(), event.getEntityId());
        return eventsRepository.create(event);
    }

    public Collection<Event> findAllByUserId(long userId) {
        log.info("Находим всех пользователей по Id = {}", userId);
        Collection<Event> events = eventsRepository.findAllByUserId(userId);
        log.info("EventService Коллекция пользователей events = {}", events);
        if (events.isEmpty()) {
            throw new NotFoundException("Событий у данного пользователя не найдено в базе данных. Пользователь - ", userId);
        }
        return events;
    }

    public Collection<Event> findAll() {
        return eventsRepository.findAll();
    }
}
