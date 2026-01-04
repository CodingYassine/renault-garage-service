package com.renault.garage.service;

import com.renault.garage.domain.FuelType;
import com.renault.garage.dto.GarageSummaryDto;
import com.renault.garage.dto.VehicleSummaryDto;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.mapper.VehicleMapper;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final VehicleRepository vehicleRepository;
    private final GarageRepository garageRepository;
    private final VehicleMapper vehicleMapper;
    private final GarageMapper garageMapper;

    /**
     * Retourne une page de VehicleSummaryDto recherchée par modèle.
     */
    public Page<VehicleSummaryDto> vehiclesByModel(String modele, Pageable pageable) {
        return vehicleRepository.findByModele(modele, pageable)
                .map(vehicleMapper::toSummaryDto);
    }

    /**
     * Retourne la liste de GarageSummaryDto par FuelType.
     */
    public List<GarageSummaryDto> garagesByFuelType(FuelType fuelType) {
        return garageRepository.findGaragesByFuelType(fuelType).stream()
                .map(garageMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * Retourne la liste de GarageSummaryDto par nom/type d'accessoire.
     */
    public List<GarageSummaryDto> garagesByAccessory(String nomOrType) {
        return garageRepository.findGaragesWithAccessory(nomOrType).stream()
                .map(garageMapper::toSummaryDto)
                .collect(Collectors.toList());
    }
}