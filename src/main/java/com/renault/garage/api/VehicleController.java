package com.renault.garage.api;

import com.renault.garage.domain.Vehicle;
import com.renault.garage.dto.VehicleCreateRequest;
import com.renault.garage.dto.VehicleSummaryDto;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;
    private final VehicleRepository vehicleRepository;

    @PostMapping
    public Vehicle create(@Valid @RequestBody VehicleCreateRequest req) { return vehicleService.addVehicle(req); }

    @PutMapping("/{id}")
    public Vehicle update(@PathVariable Long id, @RequestBody Vehicle v) { return vehicleService.update(id, v); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { vehicleService.delete(id); }

    // List all vehicles (useful for quick checks)
    @GetMapping
    public List<VehicleSummaryDto> listAll() {
        return vehicleRepository.findAll().stream()
                .map(v -> new VehicleSummaryDto(
                        v.getId(),
                        v.getGarage() != null ? v.getGarage().getId() : null,
                        v.getBrand(),
                        v.getModele(),
                        v.getAnneeFabrication(),
                        v.getTypeCarburant()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/garage/{garageId}")
    public List<VehicleSummaryDto> listByGarage(@PathVariable Long garageId) {
        return vehicleRepository.findByGarage_Id(garageId)
                .stream()
                .map(v -> new VehicleSummaryDto(
                        v.getId(),
                        v.getGarage() != null ? v.getGarage().getId() : garageId,
                        v.getBrand(),
                        v.getModele(),
                        v.getAnneeFabrication(),
                        v.getTypeCarburant()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/modele/{modele}")
    public Page<VehicleSummaryDto> listByModele(@PathVariable String modele,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size) {
        return vehicleService.listByModele(modele, PageRequest.of(page, size))
                .map(v -> new VehicleSummaryDto(
                        v.getId(),
                        v.getGarage() != null ? v.getGarage().getId() : null,
                        v.getBrand(),
                        v.getModele(),
                        v.getAnneeFabrication(),
                        v.getTypeCarburant()
                ));
    }
}