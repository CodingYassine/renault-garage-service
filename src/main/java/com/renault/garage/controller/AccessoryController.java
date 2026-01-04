package com.renault.garage.controller;

import com.renault.garage.dto.AccessoryResponse;
import com.renault.garage.service.AccessoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accessories")
@RequiredArgsConstructor
public class AccessoryController {
    private final AccessoryService accessoryService;

    @PostMapping
    public AccessoryResponse create(@Valid @RequestBody AccessoryResponse req) {
        return accessoryService.create(req);
    }

    @PutMapping("/{id}")
    public AccessoryResponse update(@PathVariable Long id, @Valid @RequestBody AccessoryResponse req) {
        return accessoryService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { accessoryService.delete(id); }

    @GetMapping("/vehicle/{vehicleId}")
    public List<AccessoryResponse> listByVehicle(@PathVariable Long vehicleId) {
        return accessoryService.listByVehicle(vehicleId);
    }
}