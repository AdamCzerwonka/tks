package com.example.pasik.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealEstate {
    private UUID id;
    private String name;
    private String address;
    private double area;
    private double price;
}
