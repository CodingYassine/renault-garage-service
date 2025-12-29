package com.renault.garage.api;

import com.renault.garage.domain.FuelType;
import com.renault.garage.dto.GarageSummaryDto;
import com.renault.garage.dto.VehicleSummaryDto;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final VehicleRepository vehicleRepository;
    private final GarageRepository garageRepository;

    @GetMapping("/vehicles/model/{modele}")
    public Page<VehicleSummaryDto> vehiclesByModel(@PathVariable String modele,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size) {
        return vehicleRepository.findByModele(modele, PageRequest.of(page, size))
                .map(v -> new VehicleSummaryDto(
                        v.getId(),
                        v.getGarage() != null ? v.getGarage().getId() : null,
                        v.getBrand(),
                        v.getModele(),
                        v.getAnneeFabrication(),
                        v.getTypeCarburant()
                ));
    }

    @GetMapping("/garages/by-fuel-type")
    public List<GarageSummaryDto> garagesByFuelType(@RequestParam FuelType fuelType) {
        return garageRepository.findGaragesByFuelType(fuelType).stream()
                .map(g -> new GarageSummaryDto(g.getId(), g.getName(), g.getAddress(), g.getTelephone(), g.getEmail()))
                .collect(Collectors.toList());
    }

    @GetMapping("/garages/by-accessory")
    public List<GarageSummaryDto> garagesByAccessory(@RequestParam String nomOrType) {
        return garageRepository.findGaragesWithAccessory(nomOrType).stream()
                .map(g -> new GarageSummaryDto(g.getId(), g.getName(), g.getAddress(), g.getTelephone(), g.getEmail()))
                .collect(Collectors.toList());
    }
}