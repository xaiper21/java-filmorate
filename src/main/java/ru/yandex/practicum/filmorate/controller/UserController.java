package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.dtoclasses.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.dtoclasses.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @NonNull @RequestBody UserDto user) {
        log.trace("Контроллер создания пользователя");
        return userService.create(user);
    }

    @PutMapping
    public UserDto update(@Valid @NonNull @RequestBody UserDto user) {
        log.trace("Контроллер обновление пользователя");
        return userService.update(user);
    }

    @GetMapping
    public Collection<UserDto> findAll() {
        log.trace("Контроллер получения пользователей");
        return userService.findAll();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public boolean addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.trace("Добавление в друзья");
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public boolean deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.trace("Удаление дружбы");
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDto> findAllFriends(@PathVariable long id) {
        log.trace("Получение списка друзей пользователя");
        return userService.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserDto> findMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        log.trace("Получение общих друзей");
        return userService.findMutualFriends(id, otherId);
    }

    @GetMapping("/{id}/recommendations")
    public Collection<FilmResponseDto> recommendationMovies(@PathVariable long id) {
        return userService.recommendationMovies(id);
    }
}