package com.example.tks.rest.aggregates;

import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Administrator;
import com.example.tks.ports.userinterface.AdministratorServicePort;
import com.example.tks.rest.model.User.UserResponse;

import java.util.List;
import java.util.UUID;

public class AdministratorServiceAdapter implements AdministratorServicePort<UserResponse> {
    @Override
    public List<UserResponse> get() {
        return null;
    }

    @Override
    public List<UserResponse> findAdministratorsByLogin(String login) {
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
    public UserResponse create(Administrator administrator) throws LoginAlreadyTakenException {
        return null;
    }

    @Override
    public UserResponse update(Administrator administrator) throws NotFoundException {
        return null;
    }

    @Override
    public void setActiveStatus(UUID id, boolean active) throws NotFoundException {

    }
}
