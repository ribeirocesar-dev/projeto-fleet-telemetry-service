package com.fleet.telemetry.controller;

import com.fleet.telemetry.domain.entity.AlertEntity;
import com.fleet.telemetry.dto.TelemetryPayloadDTO;
import com.fleet.telemetry.dto.VehicleResponseDTO;
import com.fleet.telemetry.service.VehicleQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleQueryService vehicleQueryService;

    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleQueryService.getAllVehicles());
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDTO> getVehicleById(@PathVariable UUID vehicleId) {
        return vehicleQueryService.getVehicleById(vehicleId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{vehicleId}/last-position")
    public ResponseEntity<TelemetryPayloadDTO> getLastPosition(@PathVariable UUID vehicleId) {
        return vehicleQueryService.getVehicleLastPosition(vehicleId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{vehicleId}/alerts")
    public ResponseEntity<Page<AlertEntity>> getVehicleAlerts(
            @PathVariable UUID vehicleId,
            @PageableDefault(size = 10, sort = "timestamp") Pageable pageable) {
        return ResponseEntity.ok(vehicleQueryService.getAlertsByVehicle(vehicleId, pageable));
    }
}