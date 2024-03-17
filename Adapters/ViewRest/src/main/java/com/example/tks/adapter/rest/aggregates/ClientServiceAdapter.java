package com.example.tks.adapter.rest.aggregates;

import com.example.tks.adapter.rest.model.Client.ClientCreateRequest;
import com.example.tks.adapter.rest.model.Client.ClientUpdateRequest;
import com.example.tks.adapter.rest.model.User.UserResponse;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Client;
import com.example.tks.core.services.interfaces.ClientService;
import com.example.tks.ports.userinterface.ClientServicePort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientServiceAdapter implements ClientServicePort<UserResponse, ClientCreateRequest, ClientUpdateRequest> {
    private final ClientService service;

    public ClientServiceAdapter(ClientService clientService) {
        this.service = clientService;
    }

    @Override
    public UserResponse getById(UUID id) throws NotFoundException {
        return UserResponse.fromUser(service.getById(id));
    }

    @Override
    public UserResponse getByLogin(String login) throws NotFoundException {
        return UserResponse.fromUser(service.getByLogin(login));
    }

    @Override
    public List<UserResponse> get() {
        return service.get().stream().map(UserResponse::fromUser).toList();
    }

    @Override
    public List<UserResponse> findAllByLogin(String login) {
        return service.findAllByLogin(login).stream().map(UserResponse::fromUser).toList();
    }

    @Override
    public UserResponse create(ClientCreateRequest clientCreateRequest) throws LoginAlreadyTakenException {
        Client createdClient = service.create(clientCreateRequest.ToClient());
        return UserResponse.fromUser(createdClient);
    }

    @Override
    public UserResponse update(ClientUpdateRequest clientUpdateRequest) throws NotFoundException {
        Client updatedClient = service.update(clientUpdateRequest.ToClient());
        return UserResponse.fromUser(updatedClient);
    }

    @Override
    public void setActiveStatus(UUID id, boolean active) throws NotFoundException {
        service.setActiveStatus(id, active);
    }
}
