package com.example.pasik.managers.impl;

import com.example.pasik.managers.UserManager;
import com.example.pasik.model.User;
import com.example.pasik.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserManagerImpl implements UserManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<User> getAll(String filter) {
        return userRepository.getAll(filter);
    }

    @Override
    public User getById(UUID id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByLogin(String login) {
        return userRepository.getByLogin(login);
    }

    @Override
    public User updatePassword(String login, String oldPassword, String newPassword) {
        String password = passwordEncoder.encode(newPassword);
        oldPassword = passwordEncoder.encode(oldPassword);
        User user = userRepository.getByLogin(login);

        if (user == null) {
            return null;
        }

        if (!user.getPassword().equals(oldPassword)) {
            return null;
        }

        return userRepository.updatePassword(login, password);
    }
}
