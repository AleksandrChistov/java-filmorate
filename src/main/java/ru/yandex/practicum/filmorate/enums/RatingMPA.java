package ru.yandex.practicum.filmorate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
@ToString
public enum RatingMPA {
    G("G"), // нет возрастных ограничений
    PG("PG"), // детям рекомендуется смотреть фильм с родителями
    PG13("PG-13"), // детям до 13 лет просмотр не желателен
    R("R"), // лицам до 17 лет просматривать фильм можно только в присутствии взрослого
    NC17("NC-17"); // лицам до 18 лет просмотр запрещён

    private final String title;

    public static RatingMPA fromTitle(String title) {
        return Arrays.stream(RatingMPA.values())
                .filter(rating -> rating.getTitle().equals(title))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Рейтинг MPA не был найден с именем: " + title));
    }
}
