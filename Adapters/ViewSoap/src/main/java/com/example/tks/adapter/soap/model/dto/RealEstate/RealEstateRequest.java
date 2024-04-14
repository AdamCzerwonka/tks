package com.example.tks.adapter.soap.model.dto.RealEstate;

import com.example.tks.core.domain.model.RealEstate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
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
