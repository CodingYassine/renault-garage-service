package com.renault.garage.api;

import com.renault.garage.domain.Garage;
import com.renault.garage.service.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/garages")
@RequiredArgsConstructor
public class GarageController {
    private final GarageService garageService;

    @PostMapping
    public Garage create(@RequestBody Garage g) { return garageService.create(g); }

    @GetMapping("/{id}")
    public Optional<Garage> get(@PathVariable Long id) { return garageService.get(id); }

    @PutMapping("/{id}")
    public Garage update(@PathVariable Long id, @RequestBody Garage g) { return garageService.update(id, g); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { garageService.delete(id); }

    @GetMapping
    public Page<Garage> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String order) {
        Sort s = order.equalsIgnoreCase("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        return garageService.list(PageRequest.of(page, size, s));
    }
}
