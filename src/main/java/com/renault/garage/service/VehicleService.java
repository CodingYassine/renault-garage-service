package com.renault.garage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.domain.Garage;
import com.renault.garage.domain.Vehicle;
import com.renault.garage.dto.VehicleCreateRequest;
import com.renault.garage.dto.VehicleSummaryDto;
import com.renault.garage.events.VehicleCreatedEvent;
import com.renault.garage.mapper.VehicleMapper;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service métier pour les véhicules.
 * - centralise le mapping DTO <-> entity via VehicleMapper (MapStruct)
 * - gère l'association Garage (récupère l'entité gérée depuis GarageRepository)
 * - publie l'événement vehicle.created sur Kafka après création
 */
@Service
@RequiredArgsConstructor
public class VehicleService {
    private static final int MAX_VEHICLES_PER_GARAGE = 50;

    private final VehicleRepository vehicleRepository;
    private final GarageRepository garageRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final VehicleMapper vehicleMapper;

    /**
     * Topic Kafka pour les événements vehicle.created.
     * Valeur par défaut "vehicle.created" si la propriété n'est pas fournie.
     */
    @Value("${garage.events.vehicle-created-topic:vehicle.created}")
    private String vehicleCreatedTopic;

    /**
     * Crée un véhicule à partir d'un DTO et retourne le DTO résumé.
     */
    @Transactional
    public VehicleSummaryDto addVehicle(VehicleCreateRequest req) {
        if (req.getGarageId() == null) {
            throw new IllegalArgumentException("Le garage est requis (garageId).");
        }
        Garage g = garageRepository.findById(req.getGarageId())
                .orElseThrow(() -> new IllegalArgumentException("Garage inexistant: " + req.getGarageId()));

        long count = vehicleRepository.countByGarage_Id(req.getGarageId());
        if (count >= MAX_VEHICLES_PER_GARAGE) {
            throw new IllegalStateException("Quota dépassé: un garage ne peut stocker que 50 véhicules.");
        }

        // MapStruct mappe les champs simples ; on associe explicitement l'entité Garage g gérée.
        Vehicle v = vehicleMapper.toEntity(req);
        v.setGarage(g);
        Vehicle saved = vehicleRepository.save(v);

        // Construire et publier l'événement (payload contient event = VEHICLE_CREATED, etc.)
        VehicleCreatedEvent event = new VehicleCreatedEvent("VEHICLE_CREATED", saved.getId(), g.getId(), saved.getModele());
        publishVehicleCreated(event);

        return vehicleMapper.toSummaryDto(saved);
    }

    /**
     * Surcharge rétro-compatible : accepte une entité Vehicle (legacy) et renvoie un DTO résumé.
     */
    @Transactional
    public VehicleSummaryDto addVehicle(Vehicle v) {
        if (v.getGarage() == null || v.getGarage().getId() == null) {
            throw new IllegalArgumentException("Le véhicule doit référencer un garage avec un id.");
        }
        long count = vehicleRepository.countByGarage_Id(v.getGarage().getId());
        if (count >= MAX_VEHICLES_PER_GARAGE) {
            throw new IllegalStateException("Quota dépassé: un garage ne peut stocker que 50 véhicules.");
        }
        Vehicle saved = vehicleRepository.save(v);

        VehicleCreatedEvent event = new VehicleCreatedEvent("VEHICLE_CREATED", saved.getId(), saved.getGarage().getId(), saved.getModele());
        publishVehicleCreated(event);

        return vehicleMapper.toSummaryDto(saved);
    }

    /**
     * Met à jour un véhicule existant à partir d'un DTO et retourne le DTO résumé.
     */
    @Transactional
    public VehicleSummaryDto update(Long id, VehicleCreateRequest req) {
        Vehicle existing = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + id));

        // Si le garage change, valider et associer l'entité Garage gérée
        if (req.getGarageId() != null &&
                (existing.getGarage() == null || !existing.getGarage().getId().equals(req.getGarageId()))) {
            Garage g = garageRepository.findById(req.getGarageId())
                    .orElseThrow(() -> new IllegalArgumentException("Garage inexistant: " + req.getGarageId()));
            existing.setGarage(g);
        }

        // MapStruct met à jour les champs simples (brand, modele, anneeFabrication, typeCarburant, ...)
        vehicleMapper.updateFromRequest(req, existing);
        Vehicle saved = vehicleRepository.save(existing);
        return vehicleMapper.toSummaryDto(saved);
    }

    /**
     * Supprime un véhicule par id.
     */
    public void delete(Long id) {
        vehicleRepository.deleteById(id);
    }

    /**
     * Liste tous les véhicules en DTO résumé.
     */
    public List<VehicleSummaryDto> listAllDto() {
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * Liste les véhicules d'un garage en DTO résumé.
     */
    public List<VehicleSummaryDto> listByGarageDto(Long garageId) {
        return vehicleRepository.findByGarage_Id(garageId).stream()
                .map(vehicleMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * Page de véhicules par modèle en DTO résumé.
     */
    public Page<VehicleSummaryDto> listByModele(String modele, Pageable pageable) {
        return vehicleRepository.findByModele(modele, pageable)
                .map(vehicleMapper::toSummaryDto);
    }

    /* Publication Kafka */
    private void publishVehicleCreated(VehicleCreatedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(vehicleCreatedTopic, payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Erreur de sérialisation de l'événement vehicle-created", e);
        }
    }
}