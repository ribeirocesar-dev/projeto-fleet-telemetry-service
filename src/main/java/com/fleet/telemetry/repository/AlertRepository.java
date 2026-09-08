package com.fleet.telemetry.repository;

import com.fleet.telemetry.domain.entity.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<AlertEntity, UUID> {
    List<AlertEntity> findByVehicleIdOrderByTimestampDesc(UUID vehicleId);
}