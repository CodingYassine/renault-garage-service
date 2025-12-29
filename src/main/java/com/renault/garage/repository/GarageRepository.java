package com.renault.garage.repository;

import com.renault.garage.domain.FuelType;
import com.renault.garage.domain.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GarageRepository extends JpaRepository<Garage, Long> {

    @Query("select distinct g from Vehicle v join v.garage g where v.typeCarburant = :fuelType")
    List<Garage> findGaragesByFuelType(@Param("fuelType") FuelType fuelType);

    @Query("select distinct g from Accessory a join a.vehicle v join v.garage g where lower(a.nom) = lower(:term) or lower(a.type) = lower(:term)")
    List<Garage> findGaragesWithAccessory(@Param("term") String term);
}
