package com.renault.garage.service;

import com.renault.garage.domain.Garage;
import com.renault.garage.repository.GarageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GarageService {
    private final GarageRepository garageRepository;

    public Garage create(Garage g) { return garageRepository.save(g); }
    public Optional<Garage> get(Long id) { return garageRepository.findById(id); }
    public Garage update(Long id, Garage g) { g.setId(id); return garageRepository.save(g); }
    public void delete(Long id) { garageRepository.deleteById(id); }
    public Page<Garage> list(Pageable pageable) { return garageRepository.findAll(pageable); }
}
