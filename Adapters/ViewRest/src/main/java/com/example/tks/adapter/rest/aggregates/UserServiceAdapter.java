package com.example.tks.adapter.rest.aggregates;

import com.example.tks.adapter.rest.model.User.UpdatePasswordRequest;
import com.example.tks.adapter.rest.model.User.UserResponse;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.services.interfaces.UserService;
import com.example.tks.ports.userinterface.UserServicePort;

import java.util.List;
import java.util.UUID;

public class UserServiceAdapter implements UserServicePort<UserResponse, UpdatePasswordRequest> {
    private final UserService service;

    public UserServiceAdapter(UserService service) {
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
    public List<UserResponse> getAll(String filter) {
        return service.getAll(filter).stream().map(UserResponse::fromUser).toList();
    }

    @Override
    public UserResponse updatePassword(String login, UpdatePasswordRequest updatePasswordRequest) throws NotFoundException {
        return  UserResponse.fromUser(service.updatePassword(login, updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword()));
    }
}
