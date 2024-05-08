package com.example.tks.adapter.soap.model;

import com.example.tks.adapter.soap.mappers.UUIDAdapter;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@XmlType(name = "")
@XmlRootElement(name = "getRealEstateRequest", namespace = "http://www.example.com/tks/soap")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetRealEstateRequest {
    @XmlElement
    @XmlJavaTypeAdapter(type = UUID.class, value = UUIDAdapter.class)
    UUID id;
}
