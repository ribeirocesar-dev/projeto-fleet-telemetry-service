package com.fleet.telemetry.repository;

import com.fleet.telemetry.domain.entity.AlertEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<AlertEntity, UUID> {
    Page<AlertEntity> findByVehicleIdOrderByTimestampDesc(UUID vehicleId, Pageable pageable);
}