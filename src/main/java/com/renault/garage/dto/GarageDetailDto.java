package com.renault.garage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class GarageDetailDto {
    private Long id;
    private String name;
    private String address;
    private String telephone;
    private String email;
    private List<OpeningTimeDto> openingTimes;
}