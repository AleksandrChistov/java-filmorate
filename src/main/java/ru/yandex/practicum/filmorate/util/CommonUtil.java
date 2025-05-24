package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.Map;

@UtilityClass
public class CommonUtil {
    public static <T> long getNextId(Map<Long, T> items) {
        long currentMaxId = items.keySet()
                .stream()
                .max(Comparator.comparing(Long::valueOf))
                .orElse(0L);
        return ++currentMaxId;
    }


}
