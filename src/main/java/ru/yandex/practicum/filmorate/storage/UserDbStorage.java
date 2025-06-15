package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Qualifier
public class UserDbStorage implements UserStorage{
    @Override
    public void addUser(User user) {

    }

    @Override
    public User updateUser(User newUser) {
        return null;
    }

    @Override
    public boolean containsKey(long id) {
        return false;
    }

    @Override
    public User get(long id) {
        return null;
    }

    @Override
    public Collection<User> findAll() {
        return List.of();
    }

    @Override
    public long getNextId() {
        return 0;
    }

    @Override
    public Collection<User> getCollectionUsersByCollectionIds(Collection<Long> ids) {
        return List.of();
    }
}
