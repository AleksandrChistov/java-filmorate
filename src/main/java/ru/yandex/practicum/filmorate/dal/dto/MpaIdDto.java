package ru.yandex.practicum.filmorate.dal.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MpaIdDto {
    private long id;

    @JsonCreator
    public MpaIdDto(@JsonProperty("id") long id) {
        this.id = id;
    }
}
