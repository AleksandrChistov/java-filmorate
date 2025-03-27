package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.Map;

@UtilityClass
public class CommonUtil {
    public static <T> int getNextId(Map<Integer, T> items) {
        int currentMaxId = items.keySet()
                .stream()
                .max(Comparator.comparing(Integer::valueOf))
                .orElse(0);
        return ++currentMaxId;
    }
}
