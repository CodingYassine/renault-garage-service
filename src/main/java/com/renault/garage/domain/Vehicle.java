package com.renault.garage.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(indexes = {
    @Index(name = "idx_vehicle_model", columnList = "modele"),
    @Index(name = "idx_vehicle_garage", columnList = "garage_id")
})
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "garage_id")
    @JsonIgnore
    private Garage garage;

    @NotBlank private String brand;
    @NotBlank private String modele;
    @Min(1900) private int anneeFabrication;

    @Enumerated(EnumType.STRING) @NotNull
    private FuelType typeCarburant;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Accessory> accessories;
}
