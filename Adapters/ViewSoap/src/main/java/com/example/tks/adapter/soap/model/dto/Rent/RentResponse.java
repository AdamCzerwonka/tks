package com.example.tks.adapter.soap.model.dto.Rent;

import com.example.tks.core.domain.model.Rent;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class RentResponse {
    UUID id;
    String clientFirstName;
    String clientLastName;
    String realEstateName;
    LocalDate startDate;
    LocalDate endDate;

    public static RentResponse fromRent(Rent rent) {
        return new RentResponse(rent.getId(), rent.getClient().getFirstName(), rent.getClient().getLastName(), rent.getRealEstate().getName(), rent.getStartDate(), rent.getEndDate());
    }

}
