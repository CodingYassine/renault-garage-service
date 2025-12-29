package com.renault.garage.repository;

import com.renault.garage.domain.FuelType;
import com.renault.garage.domain.Garage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GarageRepository extends JpaRepository<Garage, Long> {

    @Query("select g from Garage g left join fetch g.openingTimes where g.id = :id")
    Optional<Garage> findByIdWithOpeningTimes(@Param("id") Long id);

    Page<Garage> findAll(Pageable pageable);

    @Query("select distinct g from Vehicle v join v.garage g where v.typeCarburant = :fuelType")
    List<Garage> findGaragesByFuelType(@Param("fuelType") FuelType fuelType);

    @Query("select distinct g from Accessory a join a.vehicle v join v.garage g where lower(a.nom) = lower(:term) or lower(a.type) = lower(:term)")
    List<Garage> findGaragesWithAccessory(@Param("term") String term);
}