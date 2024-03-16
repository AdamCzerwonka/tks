package com.example.tks.ports.driving;

import java.util.List;
import java.util.UUID;

public interface UserServicePort {
    List<User> getAll(String filter);

    User getById(UUID id);

    User updatePassword(String login, String oldPassword, String newPassword);

    User getByLogin(String login);
}
