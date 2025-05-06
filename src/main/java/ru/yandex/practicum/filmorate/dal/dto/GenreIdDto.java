package ru.yandex.practicum.filmorate.dal.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenreIdDto {
    private long id;

    @JsonCreator
    public GenreIdDto(@JsonProperty("id") long id) {
        this.id = id;
    }
}
