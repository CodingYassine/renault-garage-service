package com.renault.garage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Data
public class GarageCreateOrUpdateRequest {
    @NotBlank private String name;
    @NotBlank private String address;
    @NotBlank private String telephone;
    @Email @NotBlank private String email;
    private String city;

    // Map<DayOfWeek, List<OpeningTimeDto>>
    private Map<DayOfWeek, List<OpeningTimeDto>> horairesOuverture;
}