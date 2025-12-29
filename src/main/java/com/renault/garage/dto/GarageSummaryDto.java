package com.renault.garage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class GarageSummaryDto {
    private Long id;
    private String name;
    private String address;
    private String telephone;
    private String email;
}