package com.example.tks.adapter.soap.model;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "createRealEstateResponse", namespace = "http://www.example.com/tks/soap")
@XmlType(name = "")
public class CreateRealEstateResponse {
    @XmlElement
    RealEstateSoap realEstate;
}
