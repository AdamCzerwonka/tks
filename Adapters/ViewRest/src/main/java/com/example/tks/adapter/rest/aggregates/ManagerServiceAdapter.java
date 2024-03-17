package com.example.tks.adapter.rest.aggregates;

import com.example.tks.adapter.rest.model.Manager.ManagerCreateRequest;
import com.example.tks.adapter.rest.model.Manager.ManagerUpdateRequest;
import com.example.tks.adapter.rest.model.User.UserResponse;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Manager;
import com.example.tks.core.services.interfaces.ManagerService;
import com.example.tks.ports.userinterface.ManagerServicePort;

import java.util.List;
import java.util.UUID;

public class ManagerServiceAdapter implements ManagerServicePort<UserResponse, ManagerCreateRequest, ManagerUpdateRequest> {
    private final ManagerService service;

    public ManagerServiceAdapter(ManagerService service) {
        this.service = service;
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
    public UserResponse create(ManagerCreateRequest managerCreateRequest) throws LoginAlreadyTakenException {
        Manager createdManager = service.create(managerCreateRequest.ToManager());
        return UserResponse.fromUser(createdManager);
    }

    @Override
    public UserResponse update(ManagerUpdateRequest managerUpdateRequest) throws NotFoundException {
        Manager updatedManager = service.update(managerUpdateRequest.ToManager());
        return UserResponse.fromUser(updatedManager);
    }

    @Override
    public void setActiveStatus(UUID id, boolean active) throws NotFoundException {
        service.setActiveStatus(id, active);
    }
}
