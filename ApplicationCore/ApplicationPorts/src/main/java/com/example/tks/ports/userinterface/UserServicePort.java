package com.example.tks.ports.userinterface;

import com.example.tks.core.domain.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserServicePort<T, U> {
    List<T> getAll(String filter);

    T getById(UUID id) throws NotFoundException;

    T getByLogin(String login) throws NotFoundException;

    T updatePassword(String login, U rentUpdatePasswordRequest) throws NotFoundException;
}
