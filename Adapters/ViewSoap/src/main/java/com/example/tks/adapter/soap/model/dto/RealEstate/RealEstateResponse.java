package com.example.tks.adapter.soap.model.dto.RealEstate;

import com.example.tks.core.domain.model.RealEstate;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class RealEstateResponse {
    private UUID id;
    private String name;
    private String address;
    private double area;
    private double price;

    public static RealEstateResponse fromRealEstate(RealEstate realEstate) {
        return new RealEstateResponse(realEstate.getId(), realEstate.getName(), realEstate.getAddress(), realEstate.getArea(), realEstate.getPrice());
    }
}
