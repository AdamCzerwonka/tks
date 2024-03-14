package com.example.tks.adapter.data.repositories.mongo;

import com.example.tks.adapter.data.model.UserEnt;
import com.example.tks.adapter.data.repositories.UserRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MongoUserRepository implements UserRepository {
    private final MongoCollection<UserEnt> collection;

    public MongoUserRepository(MongoDatabase database) {
        this.collection = database.getCollection("users", UserEnt.class);
    }

    @Override
    public List<UserEnt> getAll(String filter) {
        List<UserEnt> mongoUsers;
        if (filter != null) {
            Bson bson = Filters.regex("login", filter);
            mongoUsers = collection.find(bson).into(new ArrayList<>());
        } else {
            mongoUsers = collection.find().into(new ArrayList<>());
        }
        return mongoUsers;
    }

    @Override
    public UserEnt getById(UUID id) {
        Bson filter = Filters.eq("_id", id);
        return collection.find(filter).first();
    }

    @Override
    public UserEnt getByLogin(String login) {
        Bson filter = Filters.eq("login", login);
        return collection.find(filter).first();
    }

    @Override
    public UserEnt updatePassword(String login, String password) {
        Bson filters = Filters.and(
                Filters.eq("login", login)
        );
        Bson updates = Updates.combine(
                Updates.set("password", password)
        );
        collection.updateOne(filters, updates);

        return getByLogin(login);
    }
}
