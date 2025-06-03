package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullObject;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
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

    public User update(User user) throws NullObject, NotFoundException {
        log.trace("Сервисный метод обновления пользователя");
        if (user == null) {
            log.trace("User = null");
            throw new NullObject(User.class);
        }
        Long userId = user.getId();
        if (!containsUser(userId)) {
            log.trace("Ошибка валидации");
            throw new NotFoundException(User.class.getName(), userId);
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

    private User getUserById(long id) throws NotFoundException {
        User user = userStorage.get(id);
        if (user == null) throw new NotFoundException(User.class.getName(), id);
        return user;
    }

    public Collection<User> findMutualFriends(long id, long otherId) {
        log.trace("Сервисный метод поиска общих друзей пользователей");
        User user = getUserById(id);
        User otherUser = getUserById(otherId);
        Collection<Long> friends = collisionFriends(user.getFriends(), otherUser.getFriends());
        if (friends.isEmpty()) return new ArrayList<>();
        return userStorage.getCollectionUsersByCollectionIds(friends);
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

    private Collection<Long> collisionFriends(Collection<Long> friends1, Collection<Long> friends2) {
        return friends1.stream()
                .filter(friends2::contains)
                .collect(Collectors.toList());
    }
}