package com.example.tks.core.services.impl;

import com.example.tks.core.services.UserManager;
import com.example.tks.ports.infrastructure.UserPort;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.tks.core.domain.model.User;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserManagerImpl implements UserManager {
    private final UserPort userRepository;
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
