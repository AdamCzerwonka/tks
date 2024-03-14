package com.example.pasik.repositories.mongo;

import com.example.pasik.model.RealEstate;
import com.example.pasik.model.dto.RealEstate.MgdRealEstate;
import com.example.pasik.repositories.RealEstateRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MongoRealEstateRepository implements RealEstateRepository {
    private final MongoCollection<MgdRealEstate> collection;

    public MongoRealEstateRepository(final MongoDatabase database) {
        this.collection = database.getCollection("realEstates", MgdRealEstate.class);
    }

    @Override
    public List<RealEstate> get() {
        return collection.find().into(new ArrayList<>()).stream().map(MgdRealEstate::toRealEstate).toList();
    }

    @Override
    public Optional<RealEstate> getById(UUID id) {
        Bson filter = Filters.eq("_id", id);
        MgdRealEstate result = collection.find(filter).first();
        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(result.toRealEstate());
    }

    @Override
    public RealEstate create(RealEstate realEstate) {
        realEstate.setId(UUID.randomUUID());
        collection.insertOne(MgdRealEstate.toMgdRealEstate(realEstate));

        return realEstate;
    }

    @Override
    public RealEstate update(RealEstate realEstate) {
        Bson updates = Updates.combine(
                Updates.set(MgdRealEstate.NAME, realEstate.getName()),
                Updates.set(MgdRealEstate.ADDRESS, realEstate.getAddress()),
                Updates.set(MgdRealEstate.AREA, realEstate.getArea()),
                Updates.set(MgdRealEstate.PRICE, realEstate.getPrice())
        );

        Bson filter = Filters.eq(MgdRealEstate.ID, realEstate.getId());

        collection.updateOne(filter, updates);
        return getById(realEstate.getId()).get();
    }

    @Override
    public void delete(UUID id) {
        Bson filter = Filters.eq("_id", id);
        collection.deleteOne(filter);
    }
}
