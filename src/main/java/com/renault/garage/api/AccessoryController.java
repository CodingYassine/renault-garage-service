package com.renault.garage.api;

import com.renault.garage.domain.Accessory;
import com.renault.garage.service.AccessoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accessories")
@RequiredArgsConstructor
public class AccessoryController {
    private final AccessoryService accessoryService;

    @PostMapping
    public Accessory create(@RequestBody Accessory a) { return accessoryService.add(a); }

    @PutMapping("/{id}")
    public Accessory update(@PathVariable Long id, @RequestBody Accessory a) { return accessoryService.update(id, a); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { accessoryService.delete(id); }

    @GetMapping("/vehicle/{vehicleId}")
    public List<Accessory> listByVehicle(@PathVariable Long vehicleId) { return accessoryService.listByVehicle(vehicleId); }
}
