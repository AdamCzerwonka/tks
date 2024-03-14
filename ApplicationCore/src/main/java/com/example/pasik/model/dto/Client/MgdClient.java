package com.example.pasik.model.dto.Client;

import com.example.pasik.model.Client;
import com.example.pasik.model.dto.User.MgdUser;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@BsonDiscriminator(key = "_clazz", value = "client")
public class MgdClient extends MgdUser {
    @BsonCreator
    public MgdClient(@BsonId UUID id,
                     @BsonProperty(FIRST_NAME) String firstName,
                     @BsonProperty(LAST_NAME) String lastName,
                     @BsonProperty(LOGIN) String login,
                     @BsonProperty(ACTIVE) Boolean active,
                     @BsonProperty("role") String role,
    @BsonProperty(PASSWORD) String password) {
        super(id, firstName, lastName, login, active, role, password);
    }

    public static MgdClient toMgdClient(Client client) {
        return new MgdClient(
                client.getId(), client.getFirstName(), client.getLastName(), client.getLogin(), client.getActive(), client.getRole(), client.getPassword());
    }

    public Client toClient() {
        return new Client(
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
