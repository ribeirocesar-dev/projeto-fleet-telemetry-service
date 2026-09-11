package com.fleet.telemetry.dto;

import java.util.UUID;

import com.fleet.telemetry.domain.enums.VehicleStatus;

public record VehicleResponseDTO(
        UUID id,
        String licensePlate,
        String model,
        VehicleStatus vehicleStatus) {
}