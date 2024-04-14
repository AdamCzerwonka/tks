package com.example.tks.adapter.soap.mappers;

import com.example.tks.adapter.soap.model.CreateRealEstateRequest;
import com.example.tks.core.domain.model.RealEstate;

public class CreateRealEstateRequestToRealEstate {
    public static RealEstate fromCreateRealEstateRequest(CreateRealEstateRequest createRealEstateRequest) {
        return new RealEstate(
                null,
                createRealEstateRequest.getName(),
                createRealEstateRequest.getAddress(),
                createRealEstateRequest.getArea(),
                createRealEstateRequest.getPrice());
    }
}
