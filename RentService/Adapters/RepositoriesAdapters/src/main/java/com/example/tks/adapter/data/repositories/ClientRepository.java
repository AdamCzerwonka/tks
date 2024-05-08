package com.example.tks.adapter.data.repositories;

import com.example.tks.adapter.data.model.ClientEnt;
import com.example.tks.core.domain.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {
    List<ClientEnt> get();

    Optional<ClientEnt> getById(UUID id);

    ClientEnt create(ClientEnt client);

    ClientEnt update(ClientEnt client) throws NotFoundException;
}
