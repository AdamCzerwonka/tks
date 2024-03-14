package com.example.pasik.repositories.mongo;

import com.example.pasik.model.Rent;
import com.example.pasik.model.dto.Client.MgdClient;
import com.example.pasik.model.dto.RealEstate.MgdRealEstate;
import com.example.pasik.model.dto.Rent.MgdRent;
import com.example.pasik.repositories.RentRepository;
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
    private final MongoCollection<MgdRent> collection;

    public MongoRentRepository(final MongoDatabase database) {
        this.collection = database.getCollection("rents", MgdRent.class);
    }

    @Override
    public List<Rent> get() {
        return collection
                .find()
                .into(new ArrayList<>())
                .stream()
                .map(MgdRent::toRent)
                .toList();
    }

    @Override
    public List<Rent> getByClientId(UUID clientId, boolean current) {
        Bson filters =  Filters.eq(MgdRent.CLIENT + "." + MgdClient.ID, clientId);

        return collection
                .find(filters)
                .into(new ArrayList<>())
                .stream()
                .map(MgdRent::toRent)
                .toList();
    }

    @Override
    public List<Rent> getByRealEstateId(UUID realEstateId, boolean current) {
        Bson filters = Filters.and(
                Filters.eq(MgdRent.REAL_ESTATE + "." + MgdRealEstate.ID, realEstateId),
                Filters.exists(MgdRent.END_DATE, !current)
        );

        return collection
                .find(filters)
                .into(new ArrayList<>())
                .stream()
                .map(MgdRent::toRent)
                .toList();
    }

    @Override
    public Optional<Rent> getById(UUID id) {
        Bson filter = Filters.eq(MgdRent.ID, id);
        MgdRent result = collection.find(filter).first();
        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(result.toRent());
    }

    @Override
    public Rent create(Rent rent) {
        rent.setId(UUID.randomUUID());
        collection.insertOne(MgdRent.toMgdRent(rent));

        return rent;
    }

    @Override
    public Rent update(Rent rent) {
        Bson updates = Updates.combine(
                Updates.set(MgdRent.CLIENT, MgdClient.toMgdClient(rent.getClient())),
                Updates.set(MgdRent.REAL_ESTATE, MgdRealEstate.toMgdRealEstate(rent.getRealEstate())),
                Updates.set(MgdRent.END_DATE, rent.getEndDate())
        );
        Bson filter = Filters.eq(MgdRent.ID, rent.getId());
        collection.updateOne(filter, updates);
        return getById(rent.getId()).orElseThrow();
    }

    @Override
    public void delete(UUID id) {
        Bson filter = Filters.eq(MgdRent.ID, id);
        collection.deleteOne(filter);
    }
}
