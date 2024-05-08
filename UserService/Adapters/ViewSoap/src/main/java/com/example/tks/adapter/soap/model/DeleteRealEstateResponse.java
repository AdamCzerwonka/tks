package com.example.tks.adapter.soap.model;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "deleteRealEstateResponse", namespace = "http://www.example.com/tks/soap")
@XmlType(name = "")
@Getter
@Setter
public class DeleteRealEstateResponse {
    @XmlElement
    private boolean success;
}
