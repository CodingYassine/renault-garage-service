package com.renault.garage.service;

import com.renault.garage.domain.Garage;
import com.renault.garage.domain.Vehicle;
import com.renault.garage.dto.VehicleCreateRequest;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private static final int MAX_VEHICLES_PER_GARAGE = 50;

    private final VehicleRepository vehicleRepository;
    private final GarageRepository garageRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final String vehicleCreatedTopic = "vehicle.created";

    @Transactional
    public Vehicle addVehicle(VehicleCreateRequest req) {
        if (req.getGarageId() == null) {
            throw new IllegalArgumentException("Le garage est requis (garageId).");
        }
        Garage g = garageRepository.findById(req.getGarageId())
                .orElseThrow(() -> new IllegalArgumentException("Garage inexistant: " + req.getGarageId()));

        long count = vehicleRepository.countByGarage_Id(req.getGarageId());
        if (count >= MAX_VEHICLES_PER_GARAGE) {
            throw new IllegalStateException("Quota dépassé: un garage ne peut stocker que 50 véhicules.");
        }

        Vehicle v = Vehicle.builder()
                .garage(g)
                .brand(req.getBrand())
                .modele(req.getModele())
                .anneeFabrication(req.getAnneeFabrication())
                .typeCarburant(req.getTypeCarburant())
                .build();

        Vehicle saved = vehicleRepository.save(v);
        String payload = String.format("{\"event\":\"VEHICLE_CREATED\",\"vehicleId\":%d,\"garageId\":%d,\"modele\":\"%s\"}",
                saved.getId(), g.getId(), saved.getModele());
        kafkaTemplate.send(vehicleCreatedTopic, payload);
        return saved;
    }

    // compat si du code existant appelle encore addVehicle(Vehicle)
    @Transactional
    public Vehicle addVehicle(Vehicle v) {
        long count = vehicleRepository.countByGarage_Id(v.getGarage().getId());
        if (count >= MAX_VEHICLES_PER_GARAGE) {
            throw new IllegalStateException("Quota dépassé: un garage ne peut stocker que 50 véhicules.");
        }
        Vehicle saved = vehicleRepository.save(v);
        String payload = String.format("{\"event\":\"VEHICLE_CREATED\",\"vehicleId\":%d,\"garageId\":%d,\"modele\":\"%s\"}",
                saved.getId(), saved.getGarage().getId(), saved.getModele());
        kafkaTemplate.send(vehicleCreatedTopic, payload);
        return saved;
    }

    public Vehicle update(Long id, Vehicle v) { v.setId(id); return vehicleRepository.save(v); }
    public void delete(Long id) { vehicleRepository.deleteById(id); }

    public Page<Vehicle> listByModele(String modele, Pageable pageable) { return vehicleRepository.findByModele(modele, pageable); }
}