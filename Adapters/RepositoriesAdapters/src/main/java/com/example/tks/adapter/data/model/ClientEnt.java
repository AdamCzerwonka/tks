package com.example.tks.adapter.data.model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "client")
public class ClientEnt extends UserEnt {
    @BsonCreator
    public ClientEnt(@BsonId UUID id,
                     @BsonProperty(FIRST_NAME) String firstName,
                     @BsonProperty(LAST_NAME) String lastName,
                     @BsonProperty(LOGIN) String login,
                     @BsonProperty(ACTIVE) Boolean active,
                     @BsonProperty("role") String role,
                     @BsonProperty(PASSWORD) String password) {
        super(id, firstName, lastName, login, active, role, password);
    }

    public final static String ID = "_id";
    public final static String FIRST_NAME = "firstName";
    public final static String LAST_NAME = "lastName";
    public final static String LOGIN = "login";
    public final static String ACTIVE = "active";
    public final static String PASSWORD = "password";
}
