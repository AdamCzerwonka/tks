package com.example.tks.adapter.data.model;

import com.example.tks.core.domain.model.Manager;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "manager")
public class ManagerEnt extends UserEnt {

    @BsonCreator
    public ManagerEnt(@BsonId UUID id,
                      @BsonProperty(FIRST_NAME) String firstName,
                      @BsonProperty(LAST_NAME) String lastName,
                      @BsonProperty(LOGIN) String login,
                      @BsonProperty(ACTIVE) Boolean active,
                      @BsonProperty("role") String role,
                      @BsonProperty(PASSWORD) String password) {
        super(id, firstName, lastName, login, active, role, password);
    }

    public static ManagerEnt toManagerEnt(Manager manager) {
        return new ManagerEnt(
                manager.getId(), manager.getFirstName(), manager.getLastName(), manager.getLogin(), manager.getActive(), manager.getRole(), manager.getPassword());
    }

    public Manager toManager() {
        return new Manager(
                getId(),
                getFirstName(),
                getLastName(),
                getLogin(),
                getActive(),
                getPassword()
        );
    }

    public final static String ID = "_id";
    public final static String FIRST_NAME = "firstName";
    public final static String LAST_NAME = "lastName";
    public final static String LOGIN = "login";
    public final static String ACTIVE = "active";
    public final static String PASSWORD = "password";
}
