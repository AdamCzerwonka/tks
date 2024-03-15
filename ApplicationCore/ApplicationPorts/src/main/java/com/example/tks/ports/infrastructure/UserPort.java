package com.example.tks.ports.infrastructure;

import com.example.tks.core.domain.model.User;

import java.util.List;
import java.util.UUID;

public interface UserPort {
    List<User> getAll(String filter);

    User getById(UUID id);

    User getByLogin(String login);

    User updatePassword(String login, String password);
}
