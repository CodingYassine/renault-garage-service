package com.renault.garage.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class OpeningTimeDto {
    @NotNull private LocalTime startTime;
    @NotNull private LocalTime endTime;
}