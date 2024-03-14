package com.example.pasik.repositories;

import com.example.pasik.model.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    List<User> getAll(String filter);

    User getById(UUID id);

    User getByLogin(String login);

    User updatePassword(String login, String password);
}
