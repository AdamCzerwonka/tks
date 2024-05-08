package com.example.tks.adapter.data.model;

import com.example.tks.core.domain.model.User;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Data
@BsonDiscriminator(key = "_clazz", value = "user")
public class UserEnt {
    @BsonCreator
    public UserEnt(@BsonId UUID id,
                   @BsonProperty("firstName") String firstName,
                   @BsonProperty("lastName") String lastName,
                   @BsonProperty("login") String login,
                   @BsonProperty("active") Boolean active,
                   @BsonProperty("role") String role,
                   @BsonProperty("password") String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.active = active;
        this.role = role;
        this.password = password;
    }

    public User toUser() {
        return new User(getId(), getFirstName(), getLastName(), getLogin(), getActive(), getRole(), getPassword());
    }

    @BsonId
    private UUID id;
    @BsonProperty("firstName")
    private String firstName;
    @BsonProperty("lastName")
    private String lastName;
    @BsonProperty("login")
    private String login;
    @BsonProperty("active")
    private Boolean active;
    @BsonProperty("role")
    private String role;
    @BsonProperty("password")
    private String password;
}
