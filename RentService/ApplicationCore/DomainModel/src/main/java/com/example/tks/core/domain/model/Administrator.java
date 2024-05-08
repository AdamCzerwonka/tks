package com.example.tks.core.domain.model;

import java.util.UUID;

public class Administrator extends User{
    public Administrator(UUID id, String firstName, String lastName, String login, Boolean active, String password) {
        super(id, firstName, lastName, login, active, "Administrator", password);
    }
}
