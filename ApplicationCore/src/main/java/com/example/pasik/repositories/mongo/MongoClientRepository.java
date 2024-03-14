package com.example.pasik.repositories.mongo;

import com.example.pasik.exceptions.LoginAlreadyTakenException;
import com.example.pasik.exceptions.NotFoundException;
import com.example.pasik.model.Client;
import com.example.pasik.model.dto.Client.MgdClient;
import com.example.pasik.repositories.ClientRepository;
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
    private final MongoCollection<MgdClient> collection;
    private final MongoCollection documentCollection;

    public MongoClientRepository(MongoDatabase database) {
        this.collection = database.getCollection("users", MgdClient.class);
        this.documentCollection = database.getCollection("users");
    }

    @Override
    public List<Client> get() {
        Bson filter = Filters.eq("_clazz", "client");

        return collection
                .find(filter)
                .into(new ArrayList<>())
                .stream()
                .map(MgdClient::toClient)
                .toList();
    }

    @Override
    public List<Client> findClientsByLogin(String login) {
        Pattern pattern = Pattern.compile(login, Pattern.CASE_INSENSITIVE);
        Bson filters = Filters.and(
                Filters.eq("_clazz", "client"),
                Filters.regex(MgdClient.LOGIN, pattern)
        );

        return collection
                .find(filters)
                .into(new ArrayList<>())
                .stream()
                .map(MgdClient::toClient)
                .toList();
    }

    @Override
    public Optional<Client> getById(UUID id) {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "client"),
                Filters.eq(MgdClient.ID, id)
        );
        MgdClient result = collection.find(filters).first();
        if (result == null) {
            return Optional.empty();
        }
        return Optional.of(result.toClient());
    }

    @Override
    public Optional<Client> getByLogin(String login) {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "client"),
                Filters.eq(MgdClient.LOGIN, login)
        );
        MgdClient result = collection.find(filters).first();
        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(result.toClient());
    }

    @Override
    public Client create(Client client) throws LoginAlreadyTakenException {
        Bson filter = Filters.eq(MgdClient.LOGIN, client.getLogin());
        Object existing = documentCollection.find(filter).first();

        if (existing != null) {
            throw new LoginAlreadyTakenException(client.getLogin());
        }

        client.setId(UUID.randomUUID());
        collection.insertOne(MgdClient.toMgdClient(client));

        return client;
    }

    @Override
    public Client update(Client client) throws NotFoundException {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "client"),
                Filters.eq(MgdClient.ID, client.getId())
        );
        Bson updates = Updates.combine(
                Updates.set(MgdClient.FIRST_NAME, client.getFirstName()),
                Updates.set(MgdClient.LAST_NAME, client.getLastName()),
                Updates.set(MgdClient.ACTIVE, client.getActive())
        );
        collection.updateOne(filters, updates);

        Optional<Client> response = getById(client.getId());

        if (response.isEmpty()) {
            throw new NotFoundException("Client with given id does not exists");
        }

        return response.get();
    }
}
