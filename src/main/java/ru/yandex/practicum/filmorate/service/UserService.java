package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@Scope("singleton")
public class UserService {
    private final UserStorage userStorage = new InMemoryUserStorage();

    public User create(User user) {
        log.trace("Сервисный метод добавление пользователя");
        checkNameAndSet(user);
        user.setId(userStorage.getNextId());
        userStorage.addUser(user);
        return user;
    }

    public User update(User user) {
        log.trace("Сервисный метод обновления пользователя");
        if (user.getId() == null || !userStorage.containsKey(user.getId())) {
            log.trace("Ошибка валидации");
            throw new NotFoundUserException("User с указанным id не найден");
        }
        checkNameAndSet(user);
        return userStorage.updateUser(user);
    }

    public Collection<User> findAll() {
        log.trace("Сервисный метод получения всех");
        return userStorage.findAll();
    }

    private void checkNameAndSet(User user) {
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
    }

    public boolean addFriend(long id, long friendId) {
        log.trace("Сервисный метод создания дружбы");
        if (!userStorage.containsKey(id) || !userStorage.containsKey(friendId))
            throw new NotFoundUserException("Пользователь не найден");
        friend(friendId, id);
        return true;
    }

    public boolean deleteFriend(long id, long friendId) {
        log.trace("Сервисный метод удаления дружбы");
        if (!userStorage.containsKey(id) || !userStorage.containsKey(friendId))
            throw new NotFoundUserException("Пользователь не найден");
        removeFriend(id, friendId);
        return true;
    }

    public Collection<User> findAllFriends(long id) {
        log.trace("Сервисный метод получение всех друзей пользователя");
        if (!userStorage.containsKey(id)) throw new NotFoundUserException("Пользователь не найден");
        User user = userStorage.get(id);
        return userStorage.findAll().stream()
                .filter(friend -> user.getFriends().contains(friend.getId()))
                .collect(Collectors.toList());
    }

    public Collection<User> findMutualFriends(long id, long otherId) {
        log.trace("Сервисный метод поиска общих друзей пользователей");
        if (!userStorage.containsKey(id) || !userStorage.containsKey(otherId))
            throw new NotFoundUserException("Какой-то из друзей не найден");
        return userStorage.findAll().stream()
                .filter(user -> collisionFriends(id, otherId).contains(user.getId()))
                .collect(Collectors.toList());
    }

    private void friend(long id, long friendId) {
        userStorage.get(id).getFriends().add(friendId);
        userStorage.get(friendId).getFriends().add(id);
    }

    private void removeFriend(long id, long friendId) {
        userStorage.get(id).getFriends().remove(friendId);
        userStorage.get(friendId).getFriends().remove(id);
    }

    private Collection<Long> collisionFriends(long user1, long user2) {
        return userStorage.get(user1).getFriends().stream()
                .filter(user -> userStorage.get(user2).getFriends().contains(user))
                .collect(Collectors.toList());
    }

    boolean containsUser(long id) {
        return userStorage.containsKey(id);
    }
}