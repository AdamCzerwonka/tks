package com.example.tks.adapter.rest.model.dto.rent;

import com.example.tks.core.domain.model.Rent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Data
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
