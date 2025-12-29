package com.renault.garage.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(indexes = {
    @Index(name = "idx_accessory_type", columnList = "type")
})
public class Accessory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @NotBlank private String nom;
    @NotBlank private String description;
    @NotNull @DecimalMin("0.0") private BigDecimal prix;
    @NotBlank private String type;
}
