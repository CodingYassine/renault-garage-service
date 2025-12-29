package com.renault.garage.events;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class VehicleEventConsumer {

    @KafkaListener(topics = "${garage.events.vehicle-created-topic}", groupId = "garage-service")
    public void onVehicleCreated(ConsumerRecord<String, String> record) {
        // TODO: implémenter la logique de consommation (audit, projection, notifications...)
        System.out.println("Vehicle event consumed: " + record.value());
    }
}
