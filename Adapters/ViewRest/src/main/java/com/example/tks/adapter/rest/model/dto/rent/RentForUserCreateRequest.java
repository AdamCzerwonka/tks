package com.example.tks.adapter.rest.model.dto.rent;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentForUserCreateRequest {
    @NotNull(message = "RealEstateId cannot be null")
    private UUID realEstateId;
    @FutureOrPresent(message = "Cannot create rent with past date")
    private LocalDate startDate;
}
