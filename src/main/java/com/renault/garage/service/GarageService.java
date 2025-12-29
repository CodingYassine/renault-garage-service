package com.renault.garage.service;

import com.renault.garage.dto.GarageDetailDto;
import com.renault.garage.dto.GarageSummaryDto;
import com.renault.garage.dto.OpeningTimeDto;
import com.renault.garage.domain.Garage;
import com.renault.garage.dto.GarageDetailDto;
import com.renault.garage.dto.GarageSummaryDto;
import com.renault.garage.dto.OpeningTimeDto;
import com.renault.garage.repository.GarageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GarageService {
    private final GarageRepository garageRepository;

    public Garage create(Garage g) { return garageRepository.save(g); }
    public Optional<Garage> get(Long id) { return garageRepository.findById(id); }
    public Garage update(Long id, Garage g) { g.setId(id); return garageRepository.save(g); }
    public void delete(Long id) { garageRepository.deleteById(id); }

    @Transactional(readOnly = true)
    public Page<GarageSummaryDto> listSummary(Pageable pageable) {
        return garageRepository.findAll(pageable)
                .map(this::toSummaryDto);
    }

    @Transactional(readOnly = true)
    public Optional<GarageDetailDto> getDetail(Long id) {
        return garageRepository.findByIdWithOpeningTimes(id)
                .map(this::toDetailDto);
    }

    private GarageSummaryDto toSummaryDto(Garage g) {
        return new GarageSummaryDto(g.getId(), g.getName(), g.getAddress(), g.getTelephone(), g.getEmail());
    }

    private GarageDetailDto toDetailDto(Garage g) {
        return new GarageDetailDto(
                g.getId(), g.getName(), g.getAddress(), g.getTelephone(), g.getEmail(),
                g.getOpeningTimes() == null ? null :
                        g.getOpeningTimes().stream()
                                .map(ot -> new OpeningTimeDto(ot.getDayOfWeek(), ot.getStartTime(), ot.getEndTime()))
                                .collect(Collectors.toList())
        );
    }
}