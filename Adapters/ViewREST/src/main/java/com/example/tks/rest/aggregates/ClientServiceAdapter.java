package com.example.tks.rest.aggregates;

import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Client;
import com.example.tks.ports.userinterface.ClientServicePort;
import com.example.tks.rest.model.User.UserResponse;

import java.util.List;
import java.util.UUID;

public class ClientServiceAdapter implements ClientServicePort<UserResponse> {
    @Override
    public List<UserResponse> get() {
        return null;
    }

    @Override
    public List<UserResponse> findClientsByLogin(String login) {
        return null;
    }

    @Override
    public UserResponse getById(UUID id) throws NotFoundException {
        return null;
    }

    @Override
    public UserResponse getByLogin(String login) throws NotFoundException {
        return null;
    }

    @Override
    public UserResponse create(Client client) throws LoginAlreadyTakenException {
        return null;
    }

    @Override
    public UserResponse update(Client client) throws NotFoundException {
        return null;
    }

    @Override
    public void setActiveStatus(UUID id, boolean active) throws NotFoundException {

    }
}
