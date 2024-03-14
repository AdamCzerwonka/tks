package com.example.pasik.model.dto.RealEstate;

import com.example.pasik.model.RealEstate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RealEstateRequest {
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
