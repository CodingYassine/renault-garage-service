package com.renault.garage.api;

import com.renault.garage.domain.Vehicle;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.service.VehicleService;
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
    private final VehicleRepository vehicleRepository;

    @PostMapping
    public Vehicle create(@RequestBody Vehicle v) { return vehicleService.addVehicle(v); }

    @PutMapping("/{id}")
    public Vehicle update(@PathVariable Long id, @RequestBody Vehicle v) { return vehicleService.update(id, v); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { vehicleService.delete(id); }

    @GetMapping("/garage/{garageId}")
    public List<Vehicle> listByGarage(@PathVariable Long garageId) { return vehicleRepository.findByGarage_Id(garageId); }

    @GetMapping("/modele/{modele}")
    public Page<Vehicle> listByModele(@PathVariable String modele,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        return vehicleService.listByModele(modele, PageRequest.of(page, size));
    }
}
