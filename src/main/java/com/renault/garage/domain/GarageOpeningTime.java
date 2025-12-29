package com.renault.garage.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class GarageOpeningTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "garage_id")
    private Garage garage;

    @Enumerated(EnumType.STRING) @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull private LocalTime startTime;
    @NotNull private LocalTime endTime;
}
