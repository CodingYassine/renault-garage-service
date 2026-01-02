package com.renault.garage.events;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for VehicleEventConsumer to verify logging behavior.
 * Since we're using @Slf4j annotation with Lombok, the logger is automatically
 * injected and we verify the method executes without errors.
 */
@ExtendWith(MockitoExtension.class)
class VehicleEventConsumerTest {

    @Test
    void shouldConsumeVehicleEventSuccessfully() {
        // Given
        VehicleEventConsumer consumer = new VehicleEventConsumer();
        String jsonPayload = "{\"event\":\"VEHICLE_CREATED\",\"vehicleId\":123,\"garageId\":1,\"modele\":\"Clio\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("vehicle.created", 0, 0L, "key", jsonPayload);

        // When/Then - should not throw any exception
        assertDoesNotThrow(() -> consumer.onVehicleCreated(record));
    }

    @Test
    void shouldHandleMultipleEventsIndependently() {
        // Given
        VehicleEventConsumer consumer = new VehicleEventConsumer();
        ConsumerRecord<String, String> record1 = new ConsumerRecord<>("vehicle.created", 0, 0L, "key1", "{\"event\":\"VEHICLE_CREATED\",\"vehicleId\":1}");
        ConsumerRecord<String, String> record2 = new ConsumerRecord<>("vehicle.created", 0, 1L, "key2", "{\"event\":\"VEHICLE_CREATED\",\"vehicleId\":2}");

        // When/Then - each event should be processed independently without errors
        assertDoesNotThrow(() -> {
            consumer.onVehicleCreated(record1);
            consumer.onVehicleCreated(record2);
        });
    }
}
