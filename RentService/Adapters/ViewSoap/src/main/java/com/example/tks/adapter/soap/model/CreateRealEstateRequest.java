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
@XmlRootElement(name = "createRealEstateRequest", namespace = "http://www.example.com/tks/soap")
public class CreateRealEstateRequest {
    @XmlElement
    private String name;
    @XmlElement
    private String address;
    @XmlElement
    private double area;
    @XmlElement
    private double price;
}