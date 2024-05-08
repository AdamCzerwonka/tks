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
    private final MongoCollection documentCollection;

    public MongoClientRepository(MongoDatabase database) {
        this.collection = database.getCollection("users", ClientEnt.class);
        this.documentCollection = database.getCollection("users");
    }

    @Override
    public List<ClientEnt> get() {
        Bson filter = Filters.eq("_clazz", "client");

        return collection
                .find(filter)
                .into(new ArrayList<>());
    }

    @Override
    public List<ClientEnt> findAllByLogin(String login) {
        Pattern pattern = Pattern.compile(login, Pattern.CASE_INSENSITIVE);
        Bson filters = Filters.and(
                Filters.eq("_clazz", "client"),
                Filters.regex(ClientEnt.LOGIN, pattern)
        );

        return collection
                .find(filters)
                .into(new ArrayList<>());
    }

    @Override
    public Optional<ClientEnt> getById(UUID id) {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "client"),
                Filters.eq(ClientEnt.ID, id)
        );
        ClientEnt result = collection.find(filters).first();
        if (result == null) {
            return Optional.empty();
        }
        return Optional.of(result);
    }

    @Override
    public Optional<ClientEnt> getByLogin(String login) {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "client"),
                Filters.eq(ClientEnt.LOGIN, login)
        );
        ClientEnt result = collection.find(filters).first();
        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(result);
    }

    @Override
    public ClientEnt create(ClientEnt client) throws LoginAlreadyTakenException {
        Bson filter = Filters.eq(ClientEnt.LOGIN, client.getLogin());
        Object existing = documentCollection.find(filter).first();

        if (existing != null) {
            throw new LoginAlreadyTakenException(client.getLogin());
        }

        client.setId(UUID.randomUUID());
        collection.insertOne(client);

        return client;
    }

    @Override
    public ClientEnt update(ClientEnt client) throws NotFoundException {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "client"),
                Filters.eq(ClientEnt.ID, client.getId())
        );
        Bson updates = Updates.combine(
                Updates.set(ClientEnt.FIRST_NAME, client.getFirstName()),
                Updates.set(ClientEnt.LAST_NAME, client.getLastName()),
                Updates.set(ClientEnt.ACTIVE, client.getActive())
        );
        collection.updateOne(filters, updates);

        Optional<ClientEnt> response = getById(client.getId());

        if (response.isEmpty()) {
            throw new NotFoundException("Client with given id does not exists");
        }

        return response.get();
    }
}
