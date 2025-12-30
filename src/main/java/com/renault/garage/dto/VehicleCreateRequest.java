package com.renault.garage.dto;

import com.renault.garage.domain.FuelType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleCreateRequest {
    @NotNull private Long garageId;
    @NotBlank private String brand;
    @NotBlank private String modele;
    @Min(1900) private int anneeFabrication;
    @NotNull private FuelType typeCarburant;
}