package com.example.tks.adapter.soap.model;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getRealEstatesResponse", namespace = "http://www.example.com/tks/soap")
@XmlType(name = "")
@Getter
@Setter
public class GetRealEstatesResponse {
    @XmlElement(name = "realEstate")
    @XmlElementWrapper(name = "realEstates")
    private List<RealEstateSoap> realEstates;
}
