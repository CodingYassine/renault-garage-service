package com.renault.garage.api;

import com.renault.garage.domain.FuelType;
import com.renault.garage.domain.Garage;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final VehicleRepository vehicleRepository;
    private final GarageRepository garageRepository;

    @GetMapping("/vehicles/model/{modele}")
    public Page<?> vehiclesByModel(@PathVariable String modele,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size) {
        return vehicleRepository.findByModele(modele, PageRequest.of(page, size));
    }

    @GetMapping("/garages/by-fuel-type")
    public List<Garage> garagesByFuelType(@RequestParam FuelType fuelType) {
        return garageRepository.findGaragesByFuelType(fuelType);
    }

    @GetMapping("/garages/by-accessory")
    public List<Garage> garagesByAccessory(@RequestParam String nomOrType) {
        return garageRepository.findGaragesWithAccessory(nomOrType);
    }
}
