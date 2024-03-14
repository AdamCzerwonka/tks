package com.example.pasik.model.dto.Rent;

import com.example.pasik.model.Rent;
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
