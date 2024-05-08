package com.example.tks.adapter.data.repositories.mongo;

import com.example.tks.adapter.data.model.ClientEnt;
import com.example.tks.adapter.data.repositories.ClientRepository;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Repository
public class MongoClientRepository implements ClientRepository {
    private final MongoCollection<ClientEnt> collection;

    public MongoClientRepository(MongoDatabase database) {
        this.collection = database.getCollection("clients", ClientEnt.class);
    }

    @Override
    public List<ClientEnt> get() {
        return collection
                .find()
                .into(new ArrayList<>());
    }

    @Override
    public Optional<ClientEnt> getById(UUID id) {
        Bson filter = Filters.eq(ClientEnt.ID, id);
        ClientEnt result = collection.find(filter).first();
        if (result == null) {
            return Optional.empty();
        }
        return Optional.of(result);
    }

    @Override
    public ClientEnt create(ClientEnt client) {
        client.setId(UUID.randomUUID());
        collection.insertOne(client);

        return client;
    }

    @Override
    public ClientEnt update(ClientEnt client) throws NotFoundException {
        Bson filter = Filters.eq(ClientEnt.ID, client.getId());
        Bson updates = Updates.combine(
                Updates.set(ClientEnt.ACTIVE, client.isActive())
        );
        collection.updateOne(filter, updates);

        Optional<ClientEnt> response = getById(client.getId());

        if (response.isEmpty()) {
            throw new NotFoundException("Client with given id does not exists");
        }

        return response.get();
    }
}
