package com.example.tks.adapter.soap.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getRealEstatesResponse", namespace = "http://www.example.com/tks/soap")
@XmlType(name = "")
@Getter
@Setter
public class GetRealEstatesRequest {
}
