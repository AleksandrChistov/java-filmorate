package ru.yandex.practicum.filmorate.dal.storage.event;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.storage.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;
import java.util.Optional;

@Repository
public class EventDbStorage extends BaseDbStorage implements EventStorage {

    private static final String ADD_EVENT = """
            INSERT INTO events (user_id, event_type, operation, entity_id, timestamp)
            VALUES (?, ?, ?, ?, ?)
            """;
    private static final String GET_EVENT = "SELECT * FROM events WHERE user_id = ? ORDER BY timestamp";

    private final RowMapper<Event> mapper;

    public EventDbStorage(JdbcTemplate jdbc, RowMapper<Event> mapper) {
        super(jdbc);
        this.mapper = mapper;
    }

    @Override
    public Event addEvent(Event event) {
        long eventId = insert(ADD_EVENT,
                event.getUserId(),
                event.getEventType().toString(),
                event.getOperation().toString(),
                event.getEntityId(),
                event.getTimestamp()
        );
        event.setEventId(eventId);
        return event;
    }

    @Override
    public List<Event> getFeedByUserId(Long userId) {
        return jdbc.query(GET_EVENT, mapper, userId);
    }

    @Override
    public List<Event> getAll() {
        return List.of();
    }

    @Override
    public Optional<Event> getById(long id) {
        return Optional.empty();
    }

    @Override
    public Event add(Event entity) {
        return null;
    }

    @Override
    public Event update(Event entity) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
