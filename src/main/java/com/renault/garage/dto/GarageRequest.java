package com.renault.garage.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor
public class GarageRequest {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private String telephone;
    @Email @NotBlank
    private String email;
    @NotBlank
    private String city;
    @Valid
    private Map<DayOfWeek, List<OpeningTimeDto>> openingTimesByDay;
}