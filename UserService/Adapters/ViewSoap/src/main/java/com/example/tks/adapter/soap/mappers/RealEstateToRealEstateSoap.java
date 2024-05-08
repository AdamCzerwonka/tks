package com.example.tks.adapter.soap.mappers;

import com.example.tks.adapter.soap.model.RealEstateResponse;
import com.example.tks.adapter.soap.model.RealEstateSoap;
import com.example.tks.core.domain.model.RealEstate;

public class RealEstateToRealEstateSoap {
    public static RealEstateSoap fromRealEstate(RealEstate realEstate) {
        return new RealEstateSoap(realEstate.getId(), realEstate.getName(), realEstate.getAddress(), realEstate.getArea(), realEstate.getPrice());
    }

    public static RealEstate toRealEstate(RealEstateSoap realEstateResponse) {
        return new RealEstate(realEstateResponse.getId(), realEstateResponse.getName(), realEstateResponse.getAddress(), realEstateResponse.getArea(), realEstateResponse.getPrice());
    }
}
