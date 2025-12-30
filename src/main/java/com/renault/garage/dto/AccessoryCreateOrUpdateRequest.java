package com.renault.garage.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccessoryCreateOrUpdateRequest {
    @NotNull private Long vehicleId;
    @NotBlank private String nom;
    @NotBlank private String description;
    @NotNull @DecimalMin("0.0") private BigDecimal prix;
    @NotBlank private String type;
}