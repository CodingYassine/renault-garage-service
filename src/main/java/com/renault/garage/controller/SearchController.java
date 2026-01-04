package com.renault.garage.controller;

import com.renault.garage.domain.FuelType;
import com.renault.garage.dto.GarageSummaryDto;
import com.renault.garage.dto.VehicleSummaryDto;
import com.renault.garage.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/vehicles/model/{modele}")
    public Page<VehicleSummaryDto> vehiclesByModel(@PathVariable String modele,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size) {
        return searchService.vehiclesByModel(modele, PageRequest.of(page, size));
    }

    @GetMapping("/garages/by-fuel-type")
    public List<GarageSummaryDto> garagesByFuelType(@RequestParam FuelType fuelType) {
        return searchService.garagesByFuelType(fuelType);
    }

    @GetMapping("/garages/by-accessory")
    public List<GarageSummaryDto> garagesByAccessory(@RequestParam String nomOrType) {
        return searchService.garagesByAccessory(nomOrType);
    }
}