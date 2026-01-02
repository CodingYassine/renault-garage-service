package com.renault.garage.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@AllArgsConstructor
public class VehicleCreatedEvent {
    private String event;
    private Long vehicleId;
    private Long garageId;
    private String modele;
}