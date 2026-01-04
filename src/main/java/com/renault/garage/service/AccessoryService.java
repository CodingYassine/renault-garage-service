package com.renault.garage.service;

import com.renault.garage.domain.Accessory;
import com.renault.garage.domain.Vehicle;
import com.renault.garage.dto.AccessoryResponse;
import com.renault.garage.mapper.AccessoryMapper;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service pour gérer les accessoires.
 * - centralise la gestion de l'association Vehicle (récupère l'entité gérée depuis VehicleRepository)
 * - utilise AccessoryMapper pour le mapping entity <-> DTO (le mapper ignore la relation vehicle pour les entrées)
 */
@Service
@RequiredArgsConstructor
public class AccessoryService {
    private final AccessoryRepository accessoryRepository;
    private final VehicleRepository vehicleRepository;
    private final AccessoryMapper accessoryMapper;

    /**
     * Crée un Accessory à partir d'un DTO et retourne le DTO complet.
     */
    @Transactional
    public AccessoryResponse create(AccessoryResponse req) {
        if (req.getVehicleId() == null) {
            throw new IllegalArgumentException("vehicleId requis pour créer un accessoire.");
        }
        Vehicle vehicle = vehicleRepository.findById(req.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle inexistant: " + req.getVehicleId()));

        Accessory accessory = accessoryMapper.toEntity(req); // mapper ignore la relation vehicle
        accessory.setVehicle(vehicle);
        Accessory saved = accessoryRepository.save(accessory);
        return accessoryMapper.toDto(saved);
    }

    /**
     * Met à jour un Accessory existant à partir d'un DTO et retourne le DTO mis à jour.
     */
    @Transactional
    public AccessoryResponse update(Long id, AccessoryResponse req) {
        Accessory existing = accessoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Accessory not found: " + id));

        // Si vehicleId changé, valider et associer l'entité Vehicle gérée
        if (req.getVehicleId() != null &&
                (existing.getVehicle() == null || !existing.getVehicle().getId().equals(req.getVehicleId()))) {
            Vehicle vehicle = vehicleRepository.findById(req.getVehicleId())
                    .orElseThrow(() -> new IllegalArgumentException("Vehicle inexistant: " + req.getVehicleId()));
            existing.setVehicle(vehicle);
        }

        // MapStruct met à jour les champs simples (nom, description, prix, type, ...)
        accessoryMapper.updateFromRequest(req, existing);
        Accessory saved = accessoryRepository.save(existing);
        return accessoryMapper.toDto(saved);
    }

    /**
     * Supprime un Accessory par id.
     */
    @Transactional
    public void delete(Long id) {
        accessoryRepository.deleteById(id);
    }

    /**
     * Retourne le DTO de l'accessoire si trouvé.
     */
    public Optional<AccessoryResponse> getDetail(Long id) {
        return accessoryRepository.findById(id).map(accessoryMapper::toDto);
    }

    /**
     * Liste tous les accessoires en DTO.
     */
    public List<AccessoryResponse> listAll() {
        return accessoryRepository.findAll().stream()
                .map(accessoryMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Liste les accessoires d'un véhicule en DTO.
     */
    public List<AccessoryResponse> listByVehicle(Long vehicleId) {
        return accessoryRepository.findByVehicle_Id(vehicleId).stream()
                .map(accessoryMapper::toDto)
                .collect(Collectors.toList());
    }
}