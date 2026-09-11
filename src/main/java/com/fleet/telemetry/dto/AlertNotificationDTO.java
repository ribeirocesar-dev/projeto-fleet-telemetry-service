package com.fleet.telemetry.dto;

import com.fleet.telemetry.domain.enums.AlertType;

import java.time.LocalDateTime;
import java.util.UUID;

public record AlertNotificationDTO(
        UUID alertId,
        UUID vehicleId,
        String licensePlate,
        AlertType type,
        String description,
        Double latitude,
        Double longitude,
        LocalDateTime timestamp) {
}