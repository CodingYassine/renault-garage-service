package com.renault.garage.it;

import com.renault.garage.domain.FuelType;
import com.renault.garage.domain.Garage;
import com.renault.garage.domain.Vehicle;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.service.VehicleService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KafkaIntegrationTest {

    private static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.3")
    );

    @DynamicPropertySource
    static void kafkaProps(DynamicPropertyRegistry registry) {
        kafka.start();
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @AfterAll
    static void tearDown() {
        kafka.stop();
    }

    @Autowired VehicleService vehicleService;
    @Autowired GarageRepository garageRepository;

    @Test
    void publishesVehicleCreatedEvent() {
        Garage g = garageRepository.save(
                Garage.builder().name("Garage A").address("1 rue X").telephone("0102030405").email("a@a.com").build()
        );
        Vehicle v = Vehicle.builder()
                .garage(g)
                .brand("Renault")
                .modele("Clio")
                .anneeFabrication(2022)
                .typeCarburant(FuelType.ESSENCE)
                .build();

        vehicleService.addVehicle(v);

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList("vehicle.created"));
            ConsumerRecord<String, String> record = consumer.poll(Duration.ofSeconds(10)).iterator().next();
            assertNotNull(record);
            assertTrue(record.value().contains("VEHICLE_CREATED"));
        }
    }
}
