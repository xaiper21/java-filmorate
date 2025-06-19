//package ru.yandex.practicum.filmorate.storage;
//
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Component
//public class InMemoryUserStorage implements UserStorage {
//    private final Map<Long, User> users = new HashMap<>();
//
//    @Override
//    public void addUser(User user) {
//        users.put(user.getId(), user);
//    }
//
//    @Override
//    public User updateUser(User newUser) {
//        User oldUser = users.get(newUser.getId());
//        oldUser.setName(newUser.getName());
//        oldUser.setLogin(newUser.getLogin());
//        oldUser.setEmail(newUser.getEmail());
//        oldUser.setBirthday(newUser.getBirthday());
//        return oldUser;
//    }
//
//    @Override
//    public boolean containsKey(long id) {
//        return users.containsKey(id);
//    }
//
////    @Override
////    public User get(long id) {
////        return users.get(id);
////    }
//
//    @Override
//    public Collection<User> findAll() {
//        return users.values().stream().collect(Collectors.toList());
//    }
//
//    @Override
//    public long getNextId() {
//        long currentMaxId = users.keySet()
//                .stream()
//                .mapToLong(id -> id)
//                .max()
//                .orElse(0);
//        return ++currentMaxId;
//    }
//
//    @Override
//    public Collection<User> getCollectionUsersByCollectionIds(Collection<Long> ids) {
//        return ids.stream()
//                .filter(users::containsKey)
//                .map(users::get)
//                .collect(Collectors.toList());
//    }
//}
