package com.renault.garage.service;

import com.renault.garage.domain.Accessory;
import com.renault.garage.repository.AccessoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessoryService {
    private final AccessoryRepository accessoryRepository;

    public Accessory add(Accessory a) { return accessoryRepository.save(a); }
    public Accessory update(Long id, Accessory a) { a.setId(id); return accessoryRepository.save(a); }
    public void delete(Long id) { accessoryRepository.deleteById(id); }
    public List<Accessory> listByVehicle(Long vehicleId) { return accessoryRepository.findByVehicle_Id(vehicleId); }
}
