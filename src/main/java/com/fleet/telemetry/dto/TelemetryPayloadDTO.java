package com.fleet.telemetry.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record TelemetryPayloadDTO(
        @NotNull UUID vehicleId,
        String licensePlate,
        @NotNull Double latitude,
        @NotNull Double longitude,
        @NotNull Double speed,
        LocalDateTime timestamp) {
}
