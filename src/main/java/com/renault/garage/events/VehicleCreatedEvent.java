package com.renault.garage.events;

import lombok.Data;

@Data
public class VehicleCreatedEvent {
    private String event;
    private Long vehicleId;
    private Long garageId;
    private String modele;
}