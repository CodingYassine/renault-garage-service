package com.renault.garage.api;

import com.renault.garage.dto.GarageDetailDto;
import com.renault.garage.dto.GarageSummaryDto;
import com.renault.garage.domain.Garage;
import com.renault.garage.service.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/garages")
@RequiredArgsConstructor
public class GarageController {
    private final GarageService garageService;

    @PostMapping
    public ResponseEntity<GarageDetailDto> create(@RequestBody Garage g) {
        Garage saved = garageService.create(g);
        return garageService.getDetail(saved.getId())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarageDetailDto> get(@PathVariable Long id) {
        Optional<GarageDetailDto> dto = garageService.getDetail(id);
        return dto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GarageDetailDto> update(@PathVariable Long id, @RequestBody Garage g) {
        Garage saved = garageService.update(id, g);
        return garageService.getDetail(saved.getId())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { garageService.delete(id); }

    @GetMapping
    public Page<GarageSummaryDto> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String order) {
        Sort s = order.equalsIgnoreCase("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        return garageService.listSummary(PageRequest.of(page, size, s));
    }
}