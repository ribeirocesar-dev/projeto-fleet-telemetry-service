package com.fleet.telemetry.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.fleet.telemetry.domain.entity.VehicleEntity;
import com.fleet.telemetry.domain.enums.VehicleStatus;
import com.fleet.telemetry.domain.enums.VehicleType;
import com.fleet.telemetry.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataLoaderConfig implements CommandLineRunner {

    private final VehicleRepository vehicleRepository;

    public void run(String... args) {
        if (vehicleRepository.count() == 0) {
            List<VehicleEntity> initialFleet = List.of(
                    VehicleEntity.builder().licensePlate("ABC-1234").model("Volvo FH 540")
                            .vehicleType(VehicleType.CAR).vehicleStatus(VehicleStatus.MOVING).build(),
                    VehicleEntity.builder().licensePlate("XYZ-9876").model("Scania R450")
                            .vehicleType(VehicleType.MOTOCYCLE).vehicleStatus(VehicleStatus.MOVING).build(),
                    VehicleEntity.builder().licensePlate("FLE-2026").model("Mercedes Actros")
                            .vehicleType(VehicleType.TRUCK).vehicleStatus(VehicleStatus.MOVING).build(),
                    VehicleEntity.builder().licensePlate("GPS-5500").model("MAN TGX 28.440")
                            .vehicleType(VehicleType.CAR).vehicleStatus(VehicleStatus.IDLE)
                            .build());
            vehicleRepository.saveAll(initialFleet);
        }
    }
}
