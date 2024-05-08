package com.example.tks.adapter.data.repositories.mongo;


import com.example.tks.adapter.data.model.RealEstateEnt;
import com.example.tks.adapter.data.repositories.RealEstateRepository;
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

@Repository
public class MongoRealEstateRepository implements RealEstateRepository {
    private final MongoCollection<RealEstateEnt> collection;

    public MongoRealEstateRepository(final MongoDatabase database) {
        this.collection = database.getCollection("realEstates", RealEstateEnt.class);
    }

    @Override
    public List<RealEstateEnt> get() {
        return collection.find().into(new ArrayList<>());
    }

    @Override
    public Optional<RealEstateEnt> getById(UUID id) {
        Bson filter = Filters.eq("_id", id);
        RealEstateEnt result = collection.find(filter).first();
        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(result);
    }

    @Override
    public RealEstateEnt create(RealEstateEnt realEstate) {
        realEstate.setId(UUID.randomUUID());
        collection.insertOne(realEstate);

        return realEstate;
    }

    @Override
    public RealEstateEnt update(RealEstateEnt realEstate) {
        Bson updates = Updates.combine(
                Updates.set(RealEstateEnt.NAME, realEstate.getName()),
                Updates.set(RealEstateEnt.ADDRESS, realEstate.getAddress()),
                Updates.set(RealEstateEnt.AREA, realEstate.getArea()),
                Updates.set(RealEstateEnt.PRICE, realEstate.getPrice())
        );

        Bson filter = Filters.eq(RealEstateEnt.ID, realEstate.getId());

        collection.updateOne(filter, updates);
        return getById(realEstate.getId()).get();
    }

    @Override
    public void delete(UUID id) {
        Bson filter = Filters.eq("_id", id);
        collection.deleteOne(filter);
    }
}
