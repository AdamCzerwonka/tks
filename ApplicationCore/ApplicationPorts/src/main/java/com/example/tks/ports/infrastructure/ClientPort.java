package com.example.tks.ports.infrastructure;

import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientPort {
    List<Client> get();

    List<Client> findClientsByLogin(String login);

    Optional<Client> getById(UUID id);

    Optional<Client> getByLogin(String login);

    Client create(Client client) throws LoginAlreadyTakenException;

    Client update(Client client) throws NotFoundException;
}
