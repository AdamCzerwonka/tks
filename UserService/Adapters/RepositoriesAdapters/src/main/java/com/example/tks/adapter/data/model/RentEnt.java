package com.example.tks.adapter.data.model;

import com.example.tks.core.domain.model.Rent;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class RentEnt {
    @BsonCreator
    public RentEnt(@BsonId UUID id,
                   @BsonProperty(CLIENT) ClientEnt client,
                   @BsonProperty(REAL_ESTATE) RealEstateEnt realEstate,
                   @BsonProperty(START_DATE) LocalDate startDate,
                   @BsonProperty(END_DATE) LocalDate endDate) {
        this.id = id;
        this.client = client;
        this.realEstate = realEstate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Rent toRent() {
        return new Rent(
                getId(),
                getClient().toClient(),
                getRealEstate().toRealEstate(),
                getStartDate(),
                getEndDate());
    }

    static public RentEnt toRentEnt(Rent rent) {
        return new RentEnt(
                rent.getId(),
                ClientEnt.toClientEnt(rent.getClient()),
                RealEstateEnt.toRealEstateEnt(rent.getRealEstate()),
                rent.getStartDate(),
                rent.getEndDate());
    }

    @BsonId
    private UUID id;
    @BsonProperty(CLIENT)
    private ClientEnt client;
    @BsonProperty(REAL_ESTATE)
    private RealEstateEnt realEstate;
    @BsonProperty(START_DATE)
    private LocalDate startDate;
    @BsonProperty(END_DATE)
    private LocalDate endDate;

    public final static String ID = "_id";
    public final static String CLIENT = "client";
    public final static String REAL_ESTATE = "realEstate";
    public final static String START_DATE = "startDate";
    public final static String END_DATE = "endDate";
}
