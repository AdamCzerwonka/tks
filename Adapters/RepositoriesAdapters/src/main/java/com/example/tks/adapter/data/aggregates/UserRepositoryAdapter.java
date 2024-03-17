package com.example.tks.adapter.data.aggregates;

import com.example.tks.adapter.data.model.UserEnt;
import com.example.tks.adapter.data.repositories.UserRepository;
import com.example.tks.core.domain.model.User;
import com.example.tks.ports.infrastructure.UserPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class UserRepositoryAdapter implements UserPort {
    private final UserRepository userRepository;

    @Override
    public List<User> getAll(String filter) {
        List<UserEnt> result = userRepository.getAll(filter);
        return result.stream().map(UserEnt::toUser).toList();
    }

    @Override
    public Optional<User> getById(UUID id) {
        return userRepository.getById(id).map(UserEnt::toUser);
    }

    @Override
    public Optional<User> getByLogin(String login) {
        return userRepository.getByLogin(login).map(UserEnt::toUser);
    }

    @Override
    public User updatePassword(String login, String password) {
        return userRepository.updatePassword(login, password).toUser();
    }
}
