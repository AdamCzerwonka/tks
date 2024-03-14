package com.example.pasik.repositories.mongo;

import com.example.pasik.exceptions.LoginAlreadyTakenException;
import com.example.pasik.exceptions.NotFoundException;
import com.example.pasik.model.Manager;
import com.example.pasik.model.dto.Manager.MgdManager;
import com.example.pasik.repositories.ManagerRepository;
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
public class MongoManagerRepository implements ManagerRepository {
    private final MongoCollection<MgdManager> collection;
    private final MongoCollection documentCollection;

    public MongoManagerRepository(MongoDatabase database) {
        this.collection = database.getCollection("users", MgdManager.class);
        this.documentCollection = database.getCollection("users");
    }

    @Override
    public List<Manager> get() {
        Bson filter = Filters.eq("_clazz", "manager");

        return collection
                .find(filter)
                .into(new ArrayList<>())
                .stream()
                .map(MgdManager::toManager)
                .toList();
    }

    @Override
    public List<Manager> findManagersByLogin(String login) {
        Pattern pattern = Pattern.compile(login, Pattern.CASE_INSENSITIVE);
        Bson filters = Filters.and(
                Filters.eq("_clazz", "manager"),
                Filters.regex(MgdManager.LOGIN, pattern)
        );

        return collection
                .find(filters)
                .into(new ArrayList<>())
                .stream()
                .map(MgdManager::toManager)
                .toList();
    }

    @Override
    public Optional<Manager> getById(UUID id) {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "manager"),
                Filters.eq(MgdManager.ID, id)
        );
        MgdManager result = collection.find(filters).first();
        if (result == null) {
            return Optional.empty();
        }
        return Optional.of(result.toManager());
    }

    @Override
    public Optional<Manager> getByLogin(String login) {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "manager"),
                Filters.eq(MgdManager.LOGIN, login)
        );
        MgdManager result = collection.find(filters).first();
        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(result.toManager());
    }

    @Override
    public Manager create(Manager manager) throws LoginAlreadyTakenException {
        Bson filter = Filters.eq(MgdManager.LOGIN, manager.getLogin());
        Object existing = documentCollection.find(filter).first();

        if (existing != null) {
            throw new LoginAlreadyTakenException(manager.getLogin());
        }

        manager.setId(UUID.randomUUID());
        collection.insertOne(MgdManager.toMgdManager(manager));

        return manager;
    }

    @Override
    public Manager update(Manager manager) throws NotFoundException {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "manager"),
                Filters.eq(MgdManager.ID, manager.getId())
        );
        Bson updates = Updates.combine(
                Updates.set(MgdManager.FIRST_NAME, manager.getFirstName()),
                Updates.set(MgdManager.LAST_NAME, manager.getLastName()),
                Updates.set(MgdManager.ACTIVE, manager.getActive())
        );
        collection.updateOne(filters, updates);

        Optional<Manager> response = getById(manager.getId());

        if (response.isEmpty()) {
            throw new NotFoundException("Manager with given id does not exists");
        }

        return response.get();
    }
}
