package com.example.tks.adapter.data.model;

import com.example.tks.core.domain.model.Client;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Data
public class ClientEnt {
    @BsonCreator
    public ClientEnt(@BsonId UUID id,
                     @BsonProperty(ACTIVE) Boolean active) {
        this.id = id;
        this.active = active;
    }

    @BsonId
    private UUID id;
    @BsonProperty(ACTIVE)
    private boolean active;

    public static ClientEnt toClientEnt(Client client) {
        return new ClientEnt(
                client.getId(), client.getActive());
    }

    public Client toClient() {
        return new Client(
                getId(),
                isActive()
        );
    }

    public final static String ID = "_id";
    public static final String ACTIVE = "active";
}
