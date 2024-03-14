package com.example.pasik.model;

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
public class Rent {
    private UUID id;
    private Client client;
    private RealEstate realEstate;
    private LocalDate startDate;
    private LocalDate endDate;
}
