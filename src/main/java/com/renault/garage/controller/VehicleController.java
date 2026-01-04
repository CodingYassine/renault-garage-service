package com.renault.garage.controller;

import com.renault.garage.dto.VehicleCreateRequest;
import com.renault.garage.dto.VehicleSummaryDto;
import com.renault.garage.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping
    public VehicleSummaryDto create(@Valid @RequestBody VehicleCreateRequest req) {
        return vehicleService.addVehicle(req);
    }

    @PutMapping("/{id}")
    public VehicleSummaryDto update(@PathVariable Long id, @Valid @RequestBody VehicleCreateRequest req) {
        return vehicleService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { vehicleService.delete(id); }

    // List all vehicles
    @GetMapping
    public List<VehicleSummaryDto> listAll() {
        return vehicleService.listAllDto();
    }

    @GetMapping("/garage/{garageId}")
    public List<VehicleSummaryDto> listByGarage(@PathVariable Long garageId) {
        return vehicleService.listByGarageDto(garageId);
    }

    @GetMapping("/modele/{modele}")
    public Page<VehicleSummaryDto> listByModele(@PathVariable String modele,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size) {
        return vehicleService.listByModele(modele, PageRequest.of(page, size));
    }
}