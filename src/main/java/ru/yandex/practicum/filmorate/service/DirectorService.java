package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.DirectorRepository;
import ru.yandex.practicum.filmorate.dto.dtoclasses.DirectorDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
@AllArgsConstructor
public class DirectorService {
    private final DirectorRepository directorRepository;

    public List<DirectorDto> getAllDirectors() {
        return directorRepository.findAll();
    }

    public DirectorDto getDirectorById(Long id) {
        return directorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Режиссер не найден", id));
    }

    public DirectorDto addDirector(DirectorDto director) {
        validateDirectorName(director.getName());
        return directorRepository.save(director);
    }

    public DirectorDto updateDirector(DirectorDto director) {
        validateDirectorName(director.getName());
        getDirectorById(director.getId());
        return directorRepository.update(director);
    }

    public void deleteDirector(Long id) {
        getDirectorById(id);
        directorRepository.delete(id);
    }

    public List<Film> getFilmsByDirector(Long directorId, String sortBy) {
        getDirectorById(directorId);
        return directorRepository.findFilmsByDirectorSorted(directorId, sortBy);
    }

    private void validateDirectorName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя режиссера не может быть пустым");
        }
    }
}