package com.example.tks.adapter.rest.model.RealEstate;

import com.example.tks.core.domain.model.RealEstate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RealEstateRequest {
    private UUID id;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotBlank(message = "Address cannot be empty")
    private String address;
    @Positive(message = "Floor area must be grater than 0")
    private double area;
    @Positive(message = "Price must be grater than 0")
    private double price;

    public RealEstate toRealEstate() {
        return new RealEstate(null, name, address, area, price);
    }
}
