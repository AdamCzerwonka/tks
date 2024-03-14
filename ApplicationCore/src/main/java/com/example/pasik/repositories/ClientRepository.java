package com.example.pasik.repositories;

import com.example.pasik.exceptions.LoginAlreadyTakenException;
import com.example.pasik.exceptions.NotFoundException;
import com.example.pasik.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {
    List<Client> get();
    List<Client> findClientsByLogin(String login);
    Optional<Client> getById(UUID id);
    Optional<Client> getByLogin(String login);
    Client create(Client client) throws LoginAlreadyTakenException;
    Client update(Client client) throws NotFoundException;
}
