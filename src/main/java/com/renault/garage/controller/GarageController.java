package com.renault.garage.controller;

import com.renault.garage.dto.GarageRequest;
import com.renault.garage.dto.GarageSummaryDto;
import com.renault.garage.service.GarageService;
import jakarta.validation.Valid;
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
    public ResponseEntity<GarageSummaryDto> create(@Valid @RequestBody GarageRequest req) {
        GarageSummaryDto dto = garageService.create(req);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retourne le résumé (summary) du garage.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GarageSummaryDto> getSummary(@PathVariable Long id) {
        Optional<GarageSummaryDto> dto = garageService.getSummary(id);
        return dto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retourne le DTO de détail (GarageRequest) pour un usage administratif/édition.
     */
    @GetMapping("/{id}/detail")
    public ResponseEntity<GarageRequest> getDetail(@PathVariable Long id) {
        Optional<GarageRequest> dto = garageService.getDetail(id);
        return dto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GarageSummaryDto> update(@PathVariable Long id, @Valid @RequestBody GarageRequest req) {
        GarageSummaryDto dto = garageService.update(id, req);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        garageService.delete(id);
        return ResponseEntity.noContent().build();
    }

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