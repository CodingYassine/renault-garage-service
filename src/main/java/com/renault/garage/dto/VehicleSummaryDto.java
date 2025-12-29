package com.renault.garage.dto;

import com.renault.garage.domain.FuelType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class VehicleSummaryDto {
    private Long id;
    private Long garageId;
    private String brand;
    private String modele;
    private int anneeFabrication;
    private FuelType typeCarburant;
}