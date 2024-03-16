package com.example.tks.ports.userinterface;

import java.util.List;
import java.util.UUID;

public interface UserServicePort<T> {
    List<T> getAll(String filter);

    T getById(UUID id);

    T updatePassword(String login, String oldPassword, String newPassword);

    T getByLogin(String login);
}
