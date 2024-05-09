package com.example.tks.adapter.data.repositories.mongo;


import com.example.tks.adapter.data.model.RealEstateEnt;
import com.example.tks.adapter.data.model.RentEnt;
import com.example.tks.adapter.data.repositories.RentRepository;
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
public class MongoRentRepository implements RentRepository {
    private final MongoCollection<RentEnt> collection;

    public MongoRentRepository(final MongoDatabase database) {
        this.collection = database.getCollection("rents", RentEnt.class);
    }

    @Override
    public List<RentEnt> get() {
        return collection
                .find()
                .into(new ArrayList<>());
    }

    @Override
    public List<RentEnt> getByClientId(UUID clientId, boolean current) {
        Bson filters = Filters.eq(RentEnt.CLIENT + "." + RentEnt.ID, clientId);

        return collection
                .find(filters)
                .into(new ArrayList<>());
    }

    @Override
    public List<RentEnt> getByRealEstateId(UUID realEstateId, boolean current) {
        Bson filters = Filters.and(
                Filters.eq(RentEnt.REAL_ESTATE + "." + RealEstateEnt.ID, realEstateId),
                Filters.exists(RentEnt.END_DATE, !current)
        );

        return collection
                .find(filters)
                .into(new ArrayList<>());
    }

    @Override
    public Optional<RentEnt> getById(UUID id) {
        Bson filter = Filters.eq(RentEnt.ID, id);
        RentEnt result = collection.find(filter).first();
        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(result);
    }

    @Override
    public RentEnt create(RentEnt rent) {
        rent.setId(UUID.randomUUID());
        collection.insertOne(rent);

        return rent;
    }

    @Override
    public RentEnt update(RentEnt rent) {
        Bson updates = Updates.combine(
                Updates.set(RentEnt.CLIENT, rent.getClient()),
                Updates.set(RentEnt.REAL_ESTATE, rent.getRealEstate()),
                Updates.set(RentEnt.END_DATE, rent.getEndDate())
        );
        Bson filter = Filters.eq(RentEnt.ID, rent.getId());
        collection.updateOne(filter, updates);
        return getById(rent.getId()).orElseThrow();
    }

    @Override
    public void delete(UUID id) {
        Bson filter = Filters.eq(RentEnt.ID, id);
        collection.deleteOne(filter);
    }
}
