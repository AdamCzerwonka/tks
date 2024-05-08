package com.example.tks.adapter.data.repositories;

import com.example.tks.adapter.data.model.UserEnt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<UserEnt> getAll(String filter);

    Optional<UserEnt> getById(UUID id);

    Optional<UserEnt> getByLogin(String login);

    UserEnt updatePassword(String login, String password);
}
