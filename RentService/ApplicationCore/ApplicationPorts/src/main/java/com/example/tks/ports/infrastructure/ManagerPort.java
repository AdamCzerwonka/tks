package com.example.tks.ports.infrastructure;

import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Manager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ManagerPort {
    List<Manager> get();

    List<Manager> findManagersByLogin(String login);

    Optional<Manager> getById(UUID id);

    Optional<Manager> getByLogin(String login);

    Manager create(Manager manager) throws LoginAlreadyTakenException;

    Manager update(Manager manager) throws NotFoundException;
}
