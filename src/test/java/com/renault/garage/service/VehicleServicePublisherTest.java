package com.renault.garage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.domain.FuelType;
import com.renault.garage.domain.Garage;
import com.renault.garage.domain.Vehicle;
import com.renault.garage.dto.VehicleCreateRequest;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class VehicleServicePublisherTest {

    @Test
    void shouldPublishEventWhenAddingEntity() {
        VehicleRepository repo = mock(VehicleRepository.class);
        GarageRepository garageRepo = mock(GarageRepository.class);
        KafkaTemplate<String, String> kafka = mock(KafkaTemplate.class);
        ObjectMapper objectMapper = new ObjectMapper();
        VehicleService service = new VehicleService(repo, garageRepo, kafka, objectMapper);

        Garage g = Garage.builder().id(1L).name("A").address("B").telephone("C").email("d@e.com").build();
        Vehicle v = Vehicle.builder()
                .garage(g)
                .brand("Renault")
                .modele("Clio")
                .anneeFabrication(2020)
                .typeCarburant(FuelType.ESSENCE)
                .build();

        Vehicle saved = Vehicle.builder()
                .id(99L).garage(g).brand(v.getBrand()).modele(v.getModele())
                .anneeFabrication(v.getAnneeFabrication()).typeCarburant(v.getTypeCarburant()).build();
        when(repo.save(v)).thenReturn(saved);
        when(repo.countByGarage_Id(1L)).thenReturn(0L);

        service.addVehicle(v);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafka, times(1)).send(topicCaptor.capture(), payloadCaptor.capture());

        assertEquals("vehicle.created", topicCaptor.getValue());
        String payload = payloadCaptor.getValue();
        assertTrue(payload.contains("\"event\":\"VEHICLE_CREATED\""));
        assertTrue(payload.contains("\"vehicleId\":99"));
        assertTrue(payload.contains("\"garageId\":1"));
        assertTrue(payload.contains("\"modele\":\"Clio\""));
    }

    @Test
    void shouldPublishEventWhenAddingViaDto() {
        VehicleRepository repo = mock(VehicleRepository.class);
        GarageRepository garageRepo = mock(GarageRepository.class);
        KafkaTemplate<String, String> kafka = mock(KafkaTemplate.class);
        ObjectMapper objectMapper = new ObjectMapper();
        VehicleService service = new VehicleService(repo, garageRepo, kafka, objectMapper);

        Garage g = Garage.builder().id(1L).name("A").address("B").telephone("C").email("d@e.com").build();
        when(garageRepo.findById(1L)).thenReturn(java.util.Optional.of(g));
        when(repo.countByGarage_Id(1L)).thenReturn(0L);

        VehicleCreateRequest req = new VehicleCreateRequest();
        req.setGarageId(1L);
        req.setBrand("Renault");
        req.setModele("Clio");
        req.setAnneeFabrication(2022);
        req.setTypeCarburant(FuelType.ESSENCE);

        Vehicle persisted = Vehicle.builder()
                .id(100L).garage(g).brand(req.getBrand()).modele(req.getModele())
                .anneeFabrication(req.getAnneeFabrication()).typeCarburant(req.getTypeCarburant()).build();
        when(repo.save(Mockito.any(Vehicle.class))).thenReturn(persisted);

        service.addVehicle(req);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafka, times(1)).send(topicCaptor.capture(), payloadCaptor.capture());

        assertEquals("vehicle.created", topicCaptor.getValue());
        String payload = payloadCaptor.getValue();
        assertTrue(payload.contains("\"event\":\"VEHICLE_CREATED\""));
        assertTrue(payload.contains("\"vehicleId\":100"));
        assertTrue(payload.contains("\"garageId\":1"));
        assertTrue(payload.contains("\"modele\":\"Clio\""));
    }
}