package com.renault.garage.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleCreatedEvent {
    @JsonProperty("event")
    private String event;
    
    @JsonProperty("vehicleId")
    private Long vehicleId;
    
    @JsonProperty("garageId")
    private Long garageId;
    
    @JsonProperty("brand")
    private String brand;
    
    @JsonProperty("modele")
    private String modele;
    
    @JsonProperty("anneeFabrication")
    private Integer anneeFabrication;
    
    @JsonProperty("typeCarburant")
    private String typeCarburant;
}