package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

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
        Long userId = user.getId();
        if (userId == null || !containsUser(userId)) {
            log.trace("Ошибка валидации");
            throw new NotFoundUserException(userId);
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
        friend(getUserById(id), getUserById(friendId));
        return true;
    }

    public boolean deleteFriend(long id, long friendId) {
        log.trace("Сервисный метод удаления дружбы");
        removeFriend(getUserById(id), getUserById(friendId));
        return true;
    }

    public Collection<User> findAllFriends(long id) {
        log.trace("Сервисный метод получение всех друзей пользователя");
        User user = getUserById(id);
        return userStorage.getCollectionUsersByCollectionIds(user.getFriends());
    }

    private User getUserById(long id) throws NotFoundUserException {
        User user = userStorage.get(id);
        if (user == null) throw new NotFoundUserException(id);
        return user;
    }

    public Collection<User> findMutualFriends(long id, long otherId) {
        log.trace("Сервисный метод поиска общих друзей пользователей");
        if (!userStorage.containsKey(id)) throw new NotFoundUserException(id);
        if (!userStorage.containsKey(otherId)) throw new NotFoundUserException(otherId);
        return userStorage.mutualFriends(id, otherId);
    }

    private void friend(User user1, User user2) {
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());
    }

    private void removeFriend(User user1, User user2) {
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
    }

    boolean containsUser(long id) {
        return userStorage.containsKey(id);
    }
}