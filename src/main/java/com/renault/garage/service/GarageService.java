package com.renault.garage.service;

import com.renault.garage.domain.Garage;
import com.renault.garage.dto.GarageRequest;
import com.renault.garage.dto.GarageSummaryDto;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.repository.GarageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GarageService {
    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;

    /**
     * Create a Garage from a GarageRequest DTO and return a GarageSummaryDto.
     */
    @Transactional
    public GarageSummaryDto create(GarageRequest req) {
        Garage entity = garageMapper.toEntity(req);
        Garage saved = garageRepository.save(entity);
        return garageMapper.toSummaryDto(saved);
    }

    /**
     * Update an existing Garage from a GarageRequest DTO and return a GarageSummaryDto.
     */
    @Transactional
    public GarageSummaryDto update(Long id, GarageRequest req) {
        Garage existing = garageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Garage not found: " + id));
        garageMapper.updateFromRequest(req, existing);
        Garage saved = garageRepository.save(existing);
        return garageMapper.toSummaryDto(saved);
    }

    /**
     * Return the Garage summary DTO (detail) by id.
     */
    public Optional<GarageSummaryDto> getDetail(Long id) {
        return garageRepository.findById(id).map(garageMapper::toSummaryDto);
    }

    /**
     * Return summaries (DTO) for listing.
     */
    public Page<GarageSummaryDto> listSummary(Pageable pageable) {
        return garageRepository.findAll(pageable).map(garageMapper::toSummaryDto);
    }

    /**
     * Delete garage by id.
     */
    public void delete(Long id) {
        garageRepository.deleteById(id);
    }
}