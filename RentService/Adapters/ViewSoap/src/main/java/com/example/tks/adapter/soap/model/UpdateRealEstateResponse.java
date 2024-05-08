package com.example.tks.adapter.soap.model;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "updateRealEstateResponse", namespace = "http://www.example.com/tks/soap")
public class UpdateRealEstateResponse {
    @XmlElement
    private RealEstateSoap realEstate;
}
