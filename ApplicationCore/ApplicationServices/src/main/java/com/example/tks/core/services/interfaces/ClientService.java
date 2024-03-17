package com.example.tks.core.services.interfaces;


import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientService {
    List<Client> get();

    List<Client> findAllByLogin(String login);

    Client getById(UUID id) throws NotFoundException;

    Client getByLogin(String login) throws NotFoundException;

    Client create(Client client) throws LoginAlreadyTakenException;

    Client update(Client client) throws NotFoundException;

    void setActiveStatus(UUID id, boolean active) throws NotFoundException;
}
