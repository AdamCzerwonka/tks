package com.example.tks.core.domain.model;

import java.util.UUID;

public class Manager extends User {
    public Manager(UUID id, String firstName, String lastName, String login, Boolean active,String password) {
        super(id, firstName, lastName, login, active, "Manager", password);
    }
}
