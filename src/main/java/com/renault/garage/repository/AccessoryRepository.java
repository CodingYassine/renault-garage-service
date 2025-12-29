package com.renault.garage.repository;

import com.renault.garage.domain.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessoryRepository extends JpaRepository<Accessory, Long> {
    List<Accessory> findByVehicle_Id(Long vehicleId);
}
