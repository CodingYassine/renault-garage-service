package com.renault.garage.service;

import com.renault.garage.domain.FuelType;
import com.renault.garage.domain.Garage;
import com.renault.garage.domain.Vehicle;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;

class VehicleServiceTest {
    @Test
    void shouldRejectWhenQuotaExceeded() {
        VehicleRepository repo = Mockito.mock(VehicleRepository.class);
        GarageRepository garageRepo = Mockito.mock(GarageRepository.class);
        KafkaTemplate<String, String> kafka = Mockito.mock(KafkaTemplate.class);
        VehicleService service = new VehicleService(repo, garageRepo, kafka);

        Garage g = Garage.builder().id(1L).name("A").address("B").telephone("C").email("d@e.com").build();
        Vehicle v = Vehicle.builder()
                .garage(g)
                .brand("Renault")
                .modele("Clio")
                .anneeFabrication(2020)
                .typeCarburant(FuelType.ESSENCE)
                .build();

        Mockito.when(repo.countByGarage_Id(1L)).thenReturn(50L);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.addVehicle(v));
        assertTrue(ex.getMessage().contains("Quota dépassé"));
    }
}
