package com.example.pasik.model.dto.RealEstate;

import com.example.pasik.model.RealEstate;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Data
public class MgdRealEstate {
    @BsonCreator
    public MgdRealEstate(
            @BsonId UUID id,
            @BsonProperty(NAME) String name,
            @BsonProperty(ADDRESS) String address,
            @BsonProperty(AREA) double area,
            @BsonProperty(PRICE) double price) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.area = area;
        this.price = price;
    }

    public static MgdRealEstate toMgdRealEstate(RealEstate realEstate) {
        return new MgdRealEstate(
                realEstate.getId(),
                realEstate.getName(),
                realEstate.getAddress(),
                realEstate.getArea(),
                realEstate.getPrice());
    }

    public RealEstate toRealEstate() {
        return new RealEstate(
                getId(),
                getName(),
                getAddress(),
                getArea(),
                getPrice()
        );
    }

    @BsonId
    private UUID id;
    @BsonProperty(NAME)
    private String name;
    @BsonProperty(ADDRESS)
    private String address;
    @BsonProperty(AREA)
    private double area;
    @BsonProperty(PRICE)
    private double price;

    public final static String ID = "_id";
    public final static String NAME = "name";
    public final static String ADDRESS = "address";
    public final static String AREA = "area";
    public final static String PRICE = "price";
}
