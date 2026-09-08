package com.fleet.telemetry.engine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fleet.telemetry.config.RabbitMQConfig;
import com.fleet.telemetry.domain.entity.VehicleEntity;
import com.fleet.telemetry.dto.TelemetryPayloadDTO;
import com.fleet.telemetry.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimulationEngine {

    private final VehicleRepository vehicleRepository;
    private final RabbitTemplate rabbitTemplate;
    private final Random random = new Random();

    private static final double BASE_LATITUDE = -19.9167;
    private static final double BASE_LONGITUDE = -43.9345;

    @Scheduled(fixedRate = 3000)
    public void generateTelemetryEvents() {
        List<VehicleEntity> vehicles = vehicleRepository.findAll();

        if (vehicles.isEmpty()) {
            log.warn("Nenhum veículo encontrado no banco para simulação.");
            return;
        }

        for (VehicleEntity vehicle : vehicles) {
            TelemetryPayloadDTO payload = createdSimularedPayload(vehicle);

            rabbitTemplate.convertAndSend(RabbitMQConfig.TELEMETRY_EXCHANGE, RabbitMQConfig.TELEMETRY_ROUTING_KEY,
                    payload);

            log.info("Ping simulado enviado -> Veículo: {} ({}) | Vel: {} km/h | Lat: {} | Long: {}",
                    vehicle.getLicensePlate(),
                    vehicle.getId(),
                    String.format("%.1f", payload.speed()),
                    String.format("%.4f", payload.latitude()),
                    String.format("%.4f", payload.longitude()));
        }
    }

    private TelemetryPayloadDTO createdSimularedPayload(VehicleEntity vehicle) {
        double latDelta = (random.nextDouble() - 0.5) * 0.01;
        double longDelta = (random.nextDouble() - 0.5) * 0.01;

        double currentLat = BASE_LATITUDE + latDelta;
        double currentLong = BASE_LONGITUDE + longDelta;
        double speed = 40.0 + (random.nextDouble() * 70.0);

        return new TelemetryPayloadDTO(vehicle.getId(), vehicle.getLicensePlate(), currentLat, currentLong, speed,
                LocalDateTime.now());
    }
}
