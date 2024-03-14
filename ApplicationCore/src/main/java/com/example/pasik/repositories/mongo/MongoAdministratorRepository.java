package com.example.pasik.repositories.mongo;

import com.example.pasik.exceptions.LoginAlreadyTakenException;
import com.example.pasik.exceptions.NotFoundException;
import com.example.pasik.model.Administrator;
import com.example.pasik.model.dto.Administrator.MgdAdministrator;
import com.example.pasik.repositories.AdministratorRepository;
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
public class MongoAdministratorRepository implements AdministratorRepository {

    private final MongoCollection<MgdAdministrator> collection;
    private final MongoCollection documentCollection;

    public MongoAdministratorRepository(MongoDatabase database) {
        this.collection = database.getCollection("users", MgdAdministrator.class);
        this.documentCollection = database.getCollection("users");
    }

    @Override
    public List<Administrator> get() {
        Bson filter = Filters.eq("_clazz", "administrator");

        return collection
                .find(filter)
                .into(new ArrayList<>())
                .stream()
                .map(MgdAdministrator::toAdministrator)
                .toList();
    }

    @Override
    public List<Administrator> findAdministratorsByLogin(String login) {
        Pattern pattern = Pattern.compile(login, Pattern.CASE_INSENSITIVE);
        Bson filters = Filters.and(
                Filters.eq("_clazz", "administrator"),
                Filters.regex(MgdAdministrator.LOGIN, pattern)
        );

        return collection
                .find(filters)
                .into(new ArrayList<>())
                .stream()
                .map(MgdAdministrator::toAdministrator)
                .toList();
    }

    @Override
    public Optional<Administrator> getById(UUID id) {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "administrator"),
                Filters.eq(MgdAdministrator.ID, id)
        );
        MgdAdministrator result = collection.find(filters).first();
        if (result == null) {
            return Optional.empty();
        }
        return Optional.of(result.toAdministrator());
    }

    @Override
    public Optional<Administrator> getByLogin(String login) {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "administrator"),
                Filters.eq(MgdAdministrator.LOGIN, login)
        );
        MgdAdministrator result = collection.find(filters).first();
        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(result.toAdministrator());
    }

    @Override
    public Administrator create(Administrator administrator) throws LoginAlreadyTakenException {
        Bson filter = Filters.eq(MgdAdministrator.LOGIN, administrator.getLogin());
        Object existing = documentCollection.find(filter).first();

        if (existing != null) {
            throw new LoginAlreadyTakenException(administrator.getLogin());
        }

        administrator.setId(UUID.randomUUID());
        collection.insertOne(MgdAdministrator.toMgdAdministrator(administrator));

        return administrator;
    }

    @Override
    public Administrator update(Administrator administrator) throws NotFoundException {
        Bson filters = Filters.and(
                Filters.eq("_clazz", "administrator"),
                Filters.eq(MgdAdministrator.ID, administrator.getId())
        );
        Bson updates = Updates.combine(
                Updates.set(MgdAdministrator.FIRST_NAME, administrator.getFirstName()),
                Updates.set(MgdAdministrator.LAST_NAME, administrator.getLastName()),
                Updates.set(MgdAdministrator.ACTIVE, administrator.getActive())
        );
        collection.updateOne(filters, updates);

        Optional<Administrator> response = getById(administrator.getId());

        if (response.isEmpty()) {
            throw new NotFoundException("Administrator with given id does not exists");
        }

        return response.get();
    }
}
