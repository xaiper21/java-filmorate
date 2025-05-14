package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User create(@RequestBody User user) {

        if (isValid(user)) {
            user.setId(nextId());
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (user.getId() == null || !users.containsKey(user.getId()))
            throw new ValidationException("User с указанным id не найден");
        User oldUser = users.get(user.getId());
        if (isValid(user)) {
            oldUser.setName(user.getName());
            oldUser.setLogin(user.getLogin());
            oldUser.setEmail(user.getEmail());
            oldUser.setBirthday(user.getBirthday());
        }
        return oldUser;
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values().stream().collect(Collectors.toList());
    }

    private boolean isValid(User user) {
        if (user.getEmail() == null || !user.getEmail().contains(String.valueOf('@')))
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ ");
        if (user.getLogin().isBlank() || user.getLogin().contains(String.valueOf(' ')))
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("дата рождения не может быть в будущем.");
        return true;
    }

    private long nextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
