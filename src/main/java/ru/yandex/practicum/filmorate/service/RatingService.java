package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dto.dtoclasses.MpaWithIdAndName;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaWithId;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class RatingService {
    private final MpaRepository mpaRepository;

    public MpaWithIdAndName findRatingById(Integer id) {
        log.trace("Получение рейтинга(mpa) по id = {} в MpaService", id);
        return mpaRepository.findOne(id).orElseThrow(
                () -> new NotFoundException(MpaWithId.class.getName(), id));
    }

    public Collection<MpaWithIdAndName> findAllRatings() {
        log.trace("Получение коллекции рейтингов(mpa) в MpaService");
        return mpaRepository.findMany();
    }
}
