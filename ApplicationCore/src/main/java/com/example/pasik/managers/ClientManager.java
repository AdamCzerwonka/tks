package com.example.pasik.managers;

import com.example.pasik.exceptions.LoginAlreadyTakenException;
import com.example.pasik.exceptions.NotFoundException;
import com.example.pasik.model.Client;

import java.util.List;
import java.util.UUID;

public interface ClientManager {
    List<Client> get();
    List<Client> findClientsByLogin(String login);
    Client getById(UUID id) throws NotFoundException;
    Client getByLogin(String login) throws NotFoundException;
    Client create(Client client) throws LoginAlreadyTakenException;
    Client update(Client client) throws NotFoundException;
    void setActiveStatus(UUID id, boolean active) throws NotFoundException;
}
