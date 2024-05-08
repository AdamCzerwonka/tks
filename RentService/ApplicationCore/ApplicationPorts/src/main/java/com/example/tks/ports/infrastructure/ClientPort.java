package com.example.tks.ports.infrastructure;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientPort {
    Optional<Client> getById(UUID id);

    List<Client> get();

    Client create(Client client);

    Client update(Client client) throws NotFoundException;
}
