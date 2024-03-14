package com.example.tks.adapter.data.repositories.mongo;

import com.example.tks.adapter.data.model.ManagerEnt;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import com.example.tks.adapter.data.repositories.ManagerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Repository
public class MongoManagerRepository implements ManagerRepository {
    private final MongoCollection<ManagerEnt> collection;
    private final MongoCollection documentCollection;

    public MongoManagerRepository(MongoDatabase database) {
        this.collection = database.getCollection("users", ManagerEnt.class);
        this.documentCollection = database.getCollection("users");
    }

    @Override
    public List<ManagerEnt> get() {
        Bson filter = Filters.eq("_clazz", "manager");

        return collection
                .find(filter)
                .into(new ArrayList<>());
    }

    @Override
    public List<ManagerEnt> findManagersByLogin(String login) {
        Pattern pattern = Pattern.compile(login, Pattern.CASE_INSENSITIVE);
        Bson filters = Filters.and(
                Filters.eq("_clazz", "manager"),
                Filters.regex(ManagerEnt.LOGIN, pattern)
        );

        return collection
                .find(filters)
                .into(new ArrayList<>());
    }

    @Override
    public Optional<ManagerEnt> getById(UUID id) {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "manager"),
                Filters.eq(ManagerEnt.ID, id)
        );
        ManagerEnt result = collection.find(filters).first();
        if (result == null) {
            return Optional.empty();
        }
        return Optional.of(result);
    }

    @Override
    public Optional<ManagerEnt> getByLogin(String login) {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "manager"),
                Filters.eq(ManagerEnt.LOGIN, login)
        );
        ManagerEnt result = collection.find(filters).first();
        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(result);
    }

    @Override
    public ManagerEnt create(ManagerEnt manager) throws LoginAlreadyTakenException {
        Bson filter = Filters.eq(ManagerEnt.LOGIN, manager.getLogin());
        Object existing = documentCollection.find(filter).first();

        if (existing != null) {
            throw new LoginAlreadyTakenException(manager.getLogin());
        }

        manager.setId(UUID.randomUUID());
        collection.insertOne(manager);

        return manager;
    }

    @Override
    public ManagerEnt update(ManagerEnt manager) throws NotFoundException {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "manager"),
                Filters.eq(ManagerEnt.ID, manager.getId())
        );
        Bson updates = Updates.combine(
                Updates.set(ManagerEnt.FIRST_NAME, manager.getFirstName()),
                Updates.set(ManagerEnt.LAST_NAME, manager.getLastName()),
                Updates.set(ManagerEnt.ACTIVE, manager.getActive())
        );
        collection.updateOne(filters, updates);

        Optional<ManagerEnt> response = getById(manager.getId());

        if (response.isEmpty()) {
            throw new NotFoundException("Manager with given id does not exists");
        }

        return response.get();
    }
}
