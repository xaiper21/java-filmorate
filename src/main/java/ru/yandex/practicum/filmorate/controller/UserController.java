package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.dtoclasses.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public UserDto create(@Valid @NonNull @RequestBody UserDto user) {
        log.trace("Контроллер создания пользователя");
        return userService.create(user);
    }

    @PutMapping("/users")
    public UserDto update(@Valid @NonNull @RequestBody UserDto user) {
        log.trace("Контроллер обновление пользователя");
        return userService.update(user);
    }

    @GetMapping("/users")
    public Collection<UserDto> findAll() {
        log.trace("Контроллер получения пользователей");
        return userService.findAll();
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public boolean addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.trace("Добавление в друзья");
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public boolean deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.trace("Удаление дружбы");
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<UserDto> findAllFriends(@PathVariable long id) {
        log.trace("Получение списка друзей пользователя");
        return userService.findAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<UserDto> findMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        log.trace("Получение общих друзей");
        return userService.findMutualFriends(id, otherId);
    }
}