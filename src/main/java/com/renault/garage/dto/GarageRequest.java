package com.renault.garage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor
public class GarageRequest {
    private Long id;
    private String name;
    private String address;
    private String telephone;
    private String email;
    // Map<DayOfWeek, List<OpeningTime>>
    private Map<DayOfWeek, List<OpeningTimeDto>> openingTimesByDay;
}