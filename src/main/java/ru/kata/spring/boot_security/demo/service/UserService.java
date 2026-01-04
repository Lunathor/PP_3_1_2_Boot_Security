package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dto.UserUpdateDto;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    void saveUser(User user, Set<Long> roleIds);
    void updateUser(UserUpdateDto userUpdateDto);
    void deleteUser(Long id);
    User findByUsername(String username);
    User getCurrentUser();
}

