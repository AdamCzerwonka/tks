package com.example.tks.rest.aggregates;

import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Manager;
import com.example.tks.ports.userinterface.ManagerServicePort;
import com.example.tks.rest.model.User.UserResponse;

import java.util.List;
import java.util.UUID;

public class ManagerServiceAdapter implements ManagerServicePort<UserResponse> {
    @Override
    public List<UserResponse> get() {
        return null;
    }

    @Override
    public List<UserResponse> findManagersByLogin(String login) {
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
    public UserResponse create(Manager manager) throws LoginAlreadyTakenException {
        return null;
    }

    @Override
    public UserResponse update(Manager manager) throws NotFoundException {
        return null;
    }

    @Override
    public void setActiveStatus(UUID id, boolean active) throws NotFoundException {

    }
}
