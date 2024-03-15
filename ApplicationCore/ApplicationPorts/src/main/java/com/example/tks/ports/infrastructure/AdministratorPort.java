package com.example.tks.ports.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Administrator;

public interface AdministratorPort {
    List<Administrator> get();

    List<Administrator> findAdministratorsByLogin(String login);

    Optional<Administrator> getById(UUID id);

    Optional<Administrator> getByLogin(String login);

    Administrator create(Administrator administratorEnt) throws LoginAlreadyTakenException;

    Administrator update(Administrator administratorEnt) throws NotFoundException;
}
