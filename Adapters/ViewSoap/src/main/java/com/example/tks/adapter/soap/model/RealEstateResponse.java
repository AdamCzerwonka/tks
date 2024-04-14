package com.example.tks.adapter.soap.model;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getRealEstateResponse", namespace = "http://www.example.com/tks/soap")
@XmlType
public class RealEstateResponse {
    @XmlElement
    private RealEstateSoap realEstate;
}
