package com.renault.garage.events;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VehicleEventConsumer {

    @KafkaListener(topics = "${garage.events.vehicle-created-topic}", groupId = "garage-service")
    public void onVehicleCreated(ConsumerRecord<String, String> record) {
        log.info("VehicleCreatedEvent reçu: value={}", record.value());
    }
}
