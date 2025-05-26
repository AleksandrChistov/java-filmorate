package ru.yandex.practicum.filmorate.dal.storage.event;

import ru.yandex.practicum.filmorate.dal.storage.Storage;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventStorage extends Storage<Event> {
    Event addEvent(Event event);

    List<Event> getFeedByUserId(Long userId);
}
