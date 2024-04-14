package com.example.tks.adapter.soap.model.dto.Rent;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
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
@XmlAccessorType(XmlAccessType.FIELD)
public class RentCreateRequest {
    @NotNull(message = "ClientId cannot be null")
    private UUID clientId;
    @NotNull(message = "RealEstateId cannot be null")
    private UUID realEstateId;
    @FutureOrPresent(message = "Cannot create rent with past date")
    private LocalDate startDate;
}
