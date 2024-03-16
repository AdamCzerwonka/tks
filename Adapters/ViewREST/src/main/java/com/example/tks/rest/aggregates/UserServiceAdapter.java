package com.example.tks.rest.aggregates;

import com.example.tks.ports.userinterface.UserServicePort;
import com.example.tks.rest.model.User.UserResponse;

import java.util.List;
import java.util.UUID;

public class UserServiceAdapter implements UserServicePort<UserResponse> {
    @Override
    public List<UserResponse> getAll(String filter) {
        return null;
    }

    @Override
    public UserResponse getById(UUID id) {
        return null;
    }

    @Override
    public UserResponse updatePassword(String login, String oldPassword, String newPassword) {
        return null;
    }

    @Override
    public UserResponse getByLogin(String login) {
        return null;
    }
}
