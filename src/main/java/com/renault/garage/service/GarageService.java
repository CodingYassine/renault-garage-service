package com.renault.garage.service;

import com.renault.garage.domain.Garage;
import com.renault.garage.dto.GarageRequest;
import com.renault.garage.dto.GarageSummaryDto;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.mapper.OpeningTimeMapper;
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
    private final OpeningTimeMapper openingTimeMapper;


    @Transactional
    public GarageSummaryDto create(GarageRequest req) {
        Garage entity = garageMapper.toEntity(req);

        // mapper utilitaire pour les horaires (associe la référence garage)
        entity.setOpeningTimes(openingTimeMapper.toEntities(req.getOpeningTimesByDay(), entity));

        Garage saved = garageRepository.save(entity);
        return garageMapper.toSummaryDto(saved);
    }


    @Transactional
    public GarageSummaryDto update(Long id, GarageRequest req) {
        Garage existing = garageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Garage not found: " + id));
        // update simple des champs (name, address, ...)
        garageMapper.updateFromRequest(req, existing);

        // gestion des opening times : si fournis dans la requête, on remplace la collection
        if (req.getOpeningTimesByDay() != null) {
            existing.setOpeningTimes(openingTimeMapper.toEntities(req.getOpeningTimesByDay(), existing));
        }

        Garage saved = garageRepository.save(existing);
        return garageMapper.toSummaryDto(saved);
    }


    public Optional<GarageRequest> getDetail(Long id) {
        return garageRepository.findByIdWithOpeningTimes(id)
                .map(g -> {
                    GarageRequest dto = garageMapper.toDetailDto(g);
                    dto.setOpeningTimesByDay(openingTimeMapper.toDtoMap(g.getOpeningTimes()));
                    return dto;
                });
    }


    public Optional<GarageSummaryDto> getSummary(Long id) {
        return garageRepository.findById(id).map(garageMapper::toSummaryDto);
    }


    public Page<GarageSummaryDto> listSummary(Pageable pageable) {
        return garageRepository.findAll(pageable).map(garageMapper::toSummaryDto);
    }

    public void delete(Long id) {
        garageRepository.deleteById(id);
    }
}