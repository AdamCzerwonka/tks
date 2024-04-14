package com.example.tks.adapter.soap.model;

import com.example.tks.adapter.soap.mappers.UUIDAdapter;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "deleteRealEstateRequest", namespace = "http://www.example.com/tks/soap")
public class DeleteRealEstateRequest {
    @XmlElement
    @XmlJavaTypeAdapter(type = UUID.class, value = UUIDAdapter.class)
    private UUID id;
}
