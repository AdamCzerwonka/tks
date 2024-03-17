package com.example.tks.adapter.rest.aggregates;

import com.example.tks.adapter.rest.model.Administrator.AdministratorCreateRequest;
import com.example.tks.adapter.rest.model.Administrator.AdministratorUpdateRequest;
import com.example.tks.adapter.rest.model.User.UserResponse;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Administrator;
import com.example.tks.core.services.interfaces.AdministratorService;
import com.example.tks.ports.userinterface.AdministratorServicePort;

import java.util.List;
import java.util.UUID;

public class AdministratorServiceAdapter implements AdministratorServicePort<UserResponse, AdministratorCreateRequest, AdministratorUpdateRequest> {
    private final AdministratorService service;

    public AdministratorServiceAdapter(AdministratorService service) {
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
    public UserResponse create(AdministratorCreateRequest administratorCreateRequest) throws LoginAlreadyTakenException {
        Administrator createdAdministrator = service.create(administratorCreateRequest.ToAdministrator());
        return UserResponse.fromUser(createdAdministrator);
    }

    @Override
    public UserResponse update(AdministratorUpdateRequest administratorUpdateRequest) throws NotFoundException {
        Administrator updatedAdministrator = service.update(administratorUpdateRequest.ToAdministrator());
        return UserResponse.fromUser(updatedAdministrator);
    }

    @Override
    public void setActiveStatus(UUID id, boolean active) throws NotFoundException {
        service.setActiveStatus(id, active);
    }
}
