package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.dtoclasses.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullObject;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Scope("singleton")
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto create(UserDto userDto) {
        log.trace("Сервисный метод добавление пользователя");
        User user = UserMapper.mapToUser(userDto);
        checkNameAndSet(user);
        user = userRepository.save(user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UserDto userDto) throws NullObject, NotFoundException {
        log.trace("Сервисный метод обновления пользователя");
        User user = UserMapper.mapToUser(userDto);
        user = userRepository.update(user);
        return UserMapper.mapToUserDto(user);
    }

    public Collection<UserDto> findAll() {
        log.trace("Сервисный метод получения всех пользователей");
        return userRepository.findMany()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    private void checkNameAndSet(User user) {
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
    }

    public boolean addFriend(long id, long friendId) {
        log.trace("Сервисный метод создания дружбы");
        containsUser(id);
        containsUser(friendId);
        userRepository.addFriend(id, friendId);
        return true;
    }

    public boolean deleteFriend(long id, long friendId) {
        log.trace("Сервисный метод удаления дружбы");
        containsUser(id);
        containsUser(friendId);
        userRepository.deleteFriend(id, friendId);
        return true;
    }

    public Collection<UserDto> findAllFriends(long id) {
        log.trace("Сервисный метод получение всех друзей пользователя");
        containsUser(id);
        return userRepository.findManyFriendsByIdUser(id)
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public Collection<UserDto> findMutualFriends(long id, long otherId) {
        log.trace("Сервисный метод поиска общих друзей пользователей");
        containsUser(id);
        containsUser(otherId);
        return userRepository.findAllMutualFriendsByIdUsers(id, otherId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public void deleteUser(long id) {
        if (!userRepository.delete(id)) {
            throw new NotFoundException(User.class.getSimpleName(), id);
        }
    }

    void containsUser(long id) {
        Optional<User> user = userRepository.findOne(id);
        if (user.isEmpty()) throw new NotFoundException(User.class.getSimpleName(), id);
    }

    public UserDto findById(Long id) {
        Optional<User> userOpt = userRepository.findOne(id);
        return userOpt.map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName(), id));
    }
}