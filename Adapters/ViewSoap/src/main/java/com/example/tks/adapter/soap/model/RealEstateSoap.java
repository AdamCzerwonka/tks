package com.example.tks.adapter.soap.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "http://www.example.com/tks/soap")
public class RealEstateSoap {
    @XmlElement
    private UUID id;
    @XmlElement
    private String name;
    @XmlElement
    private String address;
    @XmlElement
    private double area;
    @XmlElement
    private double price;
}