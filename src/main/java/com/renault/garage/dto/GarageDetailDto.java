package com.renault.garage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor
public class GarageDetailDto {
    private Long id;
    private String name;
    private String address;
    private String telephone;
    private String email;
    private Map<DayOfWeek, List<OpeningTimeDto>> openingTimesByDay;
}
