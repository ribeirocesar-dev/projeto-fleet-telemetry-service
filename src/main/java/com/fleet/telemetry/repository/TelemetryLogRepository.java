package com.fleet.telemetry.repository;

import com.fleet.telemetry.domain.entity.TelemetryLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TelemetryLogRepository extends JpaRepository<TelemetryLogEntity, UUID> {
    List<TelemetryLogEntity> findByVehicleIdOrderByTimestampDesc(UUID vehicleId);
}