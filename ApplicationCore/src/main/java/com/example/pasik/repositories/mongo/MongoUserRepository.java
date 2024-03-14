package com.example.pasik.repositories.mongo;

import com.example.pasik.model.User;
import com.example.pasik.model.dto.User.MgdUser;
import com.example.pasik.repositories.UserRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MongoUserRepository implements UserRepository {
    private final MongoCollection<MgdUser> collection;

    public MongoUserRepository(MongoDatabase database) {
        this.collection = database.getCollection("users", MgdUser.class);
    }

    @Override
    public List<User> getAll(String filter) {
        List<MgdUser> mongoUsers;
        if (filter != null) {
            Bson bson = Filters.regex("login", filter);
            mongoUsers = collection.find(bson).into(new ArrayList<>());
        } else {
            mongoUsers = collection.find().into(new ArrayList<>());
        }
        return mongoUsers.stream().map(MgdUser::MgdUserToUser).toList();
    }

    @Override
    public User getById(UUID id) {
        Bson filter = Filters.eq("_id", id);
        return MgdUser.MgdUserToUser(collection.find(filter).first());
    }

    @Override
    public User getByLogin(String login) {
        Bson filter = Filters.eq("login", login);
        MgdUser user = collection.find(filter).first();
        if (user == null) {
            return null;
        }
        return MgdUser.MgdUserToUser(user);
    }

    @Override
    public User updatePassword(String login, String password) {
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
