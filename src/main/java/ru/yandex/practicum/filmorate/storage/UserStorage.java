package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    void addUser(User user);

    User updateUser(User newUser);

    boolean containsKey(long id);

    Optional<User> get(long id);

    Collection<User> findAll();

    long getNextId();

    Collection<User> getCollectionUsersByCollectionIds(Collection<Long> ids);
}
