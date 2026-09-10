package com.fleet.telemetry.service;

import java.time.LocalDateTime;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fleet.telemetry.domain.entity.AlertEntity;
import com.fleet.telemetry.domain.enums.AlertType;
import com.fleet.telemetry.dto.AlertNotificationDTO;
import com.fleet.telemetry.dto.TelemetryPayloadDTO;
import com.fleet.telemetry.repository.AlertRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertEvaluatorService {

    private final AlertRepository alertRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String WEBSOCKET_ALERT_TOPIC = "/topic/alerts";
    private static final double SPEED_LIMIT_KMH = 100.0;

    @Transactional
    public void evaluate(TelemetryPayloadDTO payload) {
        if (payload.speed() > SPEED_LIMIT_KMH) {
            String description = String.format(
                    "Veículo %s excedeu o limite de velocidade: %.1f km/h (Limite: %.1f km/h)",
                    payload.licensePlate(), payload.speed(), SPEED_LIMIT_KMH);
            createAndSendAlert(payload, AlertType.SPEED_LIMIT_EXCEEDED, description, payload.speed(), SPEED_LIMIT_KMH);
        }
    }

    private void createAndSendAlert(TelemetryPayloadDTO payload, AlertType alertType, String description,
            double currentValue, double threshould) {
        AlertEntity alertEntity = AlertEntity.builder()
                .vehicleId(payload.vehicleId())
                .type(alertType)
                .description(description)
                .timestamp(payload.timestamp() != null ? payload.timestamp() : LocalDateTime.now())
                .build();

        alertEntity = alertRepository.save(alertEntity);

        AlertNotificationDTO alertNotification = new AlertNotificationDTO(alertEntity.getId(),
                alertEntity.getVehicleId(), payload.licensePlate(), alertType, alertEntity.getDescription(),
                payload.latitude(), payload.longitude(), alertEntity.getTimestamp());

        messagingTemplate.convertAndSend(WEBSOCKET_ALERT_TOPIC, alertNotification);

        log.warn("🚨 ALERTA DISPARADO -> [{}] Veículo: {} | Msg: {}", alertType, payload.licensePlate(), description);
    }
}
