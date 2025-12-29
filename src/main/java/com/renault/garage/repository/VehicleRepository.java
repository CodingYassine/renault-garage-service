package com.renault.garage.repository;

import com.renault.garage.domain.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    long countByGarage_Id(Long garageId);
    List<Vehicle> findByGarage_Id(Long garageId);
    Page<Vehicle> findByModele(String modele, Pageable pageable);
}
