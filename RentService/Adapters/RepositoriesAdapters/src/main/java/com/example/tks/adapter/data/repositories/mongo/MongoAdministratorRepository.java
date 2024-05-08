package com.example.tks.adapter.data.repositories.mongo;

import com.example.tks.adapter.data.model.AdministratorEnt;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import com.example.tks.adapter.data.repositories.AdministratorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Repository
public class MongoAdministratorRepository implements AdministratorRepository {

    private final MongoCollection<AdministratorEnt> collection;
    private final MongoCollection documentCollection;

    public MongoAdministratorRepository(MongoDatabase database) {
        this.collection = database.getCollection("users", AdministratorEnt.class);
        this.documentCollection = database.getCollection("users");
    }

    @Override
    public List<AdministratorEnt> get() {
        Bson filter = Filters.eq("_clazz", "administrator");

        return collection
                .find(filter)
                .into(new ArrayList<>());
    }

    @Override
    public List<AdministratorEnt> findAllByLogin(String login) {
        Pattern pattern = Pattern.compile(login, Pattern.CASE_INSENSITIVE);
        Bson filters = Filters.and(
                Filters.eq("_clazz", "administrator"),
                Filters.regex(AdministratorEnt.LOGIN, pattern)
        );

        return collection
                .find(filters)
                .into(new ArrayList<>());
    }

    @Override
    public Optional<AdministratorEnt> getById(UUID id) {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "administrator"),
                Filters.eq(AdministratorEnt.ID, id)
        );
        AdministratorEnt result = collection.find(filters).first();
        if (result == null) {
            return Optional.empty();
        }
        return Optional.of(result);
    }

    @Override
    public Optional<AdministratorEnt> getByLogin(String login) {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "administrator"),
                Filters.eq(AdministratorEnt.LOGIN, login)
        );
        AdministratorEnt result = collection.find(filters).first();
        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(result);
    }

    @Override
    public AdministratorEnt create(AdministratorEnt administrator) throws LoginAlreadyTakenException {
        Bson filter = Filters.eq(AdministratorEnt.LOGIN, administrator.getLogin());
        Object existing = documentCollection.find(filter).first();

        if (existing != null) {
            throw new LoginAlreadyTakenException(administrator.getLogin());
        }

        administrator.setId(UUID.randomUUID());
        collection.insertOne(administrator);

        return administrator;
    }

    @Override
    public AdministratorEnt update(AdministratorEnt administrator) throws NotFoundException {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "administrator"),
                Filters.eq(AdministratorEnt.ID, administrator.getId())
        );
        Bson updates = Updates.combine(
                Updates.set(AdministratorEnt.FIRST_NAME, administrator.getFirstName()),
                Updates.set(AdministratorEnt.LAST_NAME, administrator.getLastName()),
                Updates.set(AdministratorEnt.ACTIVE, administrator.getActive())
        );
        collection.updateOne(filters, updates);

        Optional<AdministratorEnt> response = getById(administrator.getId());

        if (response.isEmpty()) {
            throw new NotFoundException("Administrator with given id does not exists");
        }

        return response.get();
    }
}
