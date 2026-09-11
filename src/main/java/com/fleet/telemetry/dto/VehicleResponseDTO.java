package com.fleet.telemetry.dto;

import java.util.UUID;

import com.fleet.telemetry.domain.enums.VehicleStatus;
import com.fleet.telemetry.domain.enums.VehicleType;

public record VehicleResponseDTO(
                UUID id,
                String licensePlate,
                String model,
                VehicleStatus vehicleStatus,
                VehicleType type) {
}