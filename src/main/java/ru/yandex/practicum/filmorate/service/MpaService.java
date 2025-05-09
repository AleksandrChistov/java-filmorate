package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dto.MpaDto;
import ru.yandex.practicum.filmorate.dal.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    public MpaService(@Qualifier("mpaDbStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MpaDto> getAll() {
        return mpaStorage.getAll().stream()
                .map(MpaMapper::mapToMpaDto)
                .collect(Collectors.toList());
    }

    public MpaDto getById(long mpaId) {
        return mpaStorage.getById(mpaId)
                .map(MpaMapper::mapToMpaDto)
                .orElseThrow(() -> new NotFoundException("MPA с id = " + mpaId + " не найден."));
    }
}
