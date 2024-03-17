package com.example.tks.core.services.interfaces;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<User> getAll(String filter);

    User getById(UUID id) throws NotFoundException;

    User updatePassword(String login, String oldPassword, String newPassword) throws NotFoundException;

    User getByLogin(String login) throws NotFoundException;
}
