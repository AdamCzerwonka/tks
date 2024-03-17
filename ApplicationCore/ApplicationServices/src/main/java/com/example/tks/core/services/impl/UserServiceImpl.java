package com.example.tks.core.services.impl;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Rent;
import com.example.tks.core.services.interfaces.UserService;
import com.example.tks.ports.infrastructure.UserPort;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.tks.core.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserPort userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<User> getAll(String filter) {
        return userRepository.getAll(filter);
    }

    @Override
    public User getById(UUID id) throws NotFoundException {
        Optional<User> user = userRepository.getById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User with given id does not exist");
        }

        return user.get();
    }

    @Override
    public User getByLogin(String login) throws NotFoundException {
        Optional<User> user = userRepository.getByLogin(login);

        if (user.isEmpty()) {
            throw new NotFoundException("User with given login does not exist");
        }

        return user.get();
    }

    @Override
    public User updatePassword(String login, String oldPassword, String newPassword) throws NotFoundException {
        String password = passwordEncoder.encode(newPassword);
        oldPassword = passwordEncoder.encode(oldPassword);
        User user = getByLogin(login);

        if (!user.getPassword().equals(oldPassword)) {
            return null;
        }

        return userRepository.updatePassword(login, password);
    }
}
