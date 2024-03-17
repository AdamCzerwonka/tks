package com.example.tks.adapter.data.repositories;

import com.example.tks.adapter.data.model.ClientEnt;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {
    List<ClientEnt> get();

    List<ClientEnt> findAllByLogin(String login);

    Optional<ClientEnt> getById(UUID id);

    Optional<ClientEnt> getByLogin(String login);

    ClientEnt create(ClientEnt client) throws LoginAlreadyTakenException;

    ClientEnt update(ClientEnt client) throws NotFoundException;
}
