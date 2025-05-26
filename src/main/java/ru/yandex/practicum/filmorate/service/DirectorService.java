package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dto.DirectorDto;
import ru.yandex.practicum.filmorate.dal.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.excepton.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public List<DirectorDto> getAll() {
        log.debug("Получен запрос на получение списка всех режиссеров");
        return directorStorage.getAll().stream()
                .map(DirectorMapper::mapToDirectorDto)
                .toList();
    }

    public DirectorDto create(DirectorDto directorDto) {
        log.info("Добавление нового режиссера с именем = {}", directorDto.getName());
        Director director = directorStorage.add(DirectorMapper.mapToDirector(directorDto));
        log.info("Режиссер успешно добавлен, id = {}", directorDto.getId());
        return DirectorMapper.mapToDirectorDto(director);
    }

    public DirectorDto update(DirectorDto directorDto) {
        log.info("Обновление режиссера с id = {}", directorDto.getId());
        Director updatedDirector = directorStorage.getById(directorDto.getId())
                .map(u -> DirectorMapper.mapToDirector(directorDto))
                .orElseThrow(() -> new NotFoundException("Режиссер с id = " + directorDto.getId() + " не найден."));
        directorStorage.update(updatedDirector);
        log.info("Режиссер успешно обновлен, id = {}", directorDto.getId());
        return DirectorMapper.mapToDirectorDto(updatedDirector);
    }

    public void remove(Long directorId) {
        directorStorage.remove(directorId);
    }

    public void removeByFilmId(long filmId) {
        directorStorage.removeByFilmId(filmId);
    }

    public DirectorDto getById(Long directorId) {
        return directorStorage.getById(directorId)
                .map(DirectorMapper::mapToDirectorDto)
                .orElseThrow(() -> new NotFoundException("Режиссер с id = " + directorId + " не найден."));
    }

    public void addDirectorsByFilmId(Film film) {
        directorStorage.addDirectorsByFilmId(film);
    }

    public Map<Long,Set<Director>> getAllByFilmIds(List<Long> filmsIds) {
        return directorStorage.getAllByFilmIds(filmsIds);
    }

    public Set<DirectorDto> getAllByFilmId(long filmId) {
        return directorStorage.getAllByFilmId(filmId).stream()
                .map(DirectorMapper::mapToDirectorDto)
                .collect(Collectors.toSet());
    }
}
