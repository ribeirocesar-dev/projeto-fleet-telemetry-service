package com.fleet.telemetry.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fleet.telemetry.domain.entity.AlertEntity;
import com.fleet.telemetry.dto.TelemetryPayloadDTO;
import com.fleet.telemetry.dto.VehicleResponseDTO;
import com.fleet.telemetry.repository.AlertRepository;
import com.fleet.telemetry.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehicleQueryService {

    private final VehicleRepository vehicleRepository;
    private final AlertRepository alertRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    private static final String REDIS_VEHICLE_KEY_PREFIX = "vehicle:last_position:";

    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(v -> new VehicleResponseDTO(v.getId(), v.getLicensePlate(), v.getModel(), v.getVehicleStatus()))
                .toList();
    }

    public Optional<VehicleResponseDTO> getVehicleById(UUID vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .map(v -> new VehicleResponseDTO(v.getId(), v.getLicensePlate(), v.getModel(), v.getVehicleStatus()));
    }

    public Optional<TelemetryPayloadDTO> getVehicleLastPosition(UUID vehicleId) {
        String key = REDIS_VEHICLE_KEY_PREFIX + vehicleId;

        String json = stringRedisTemplate.opsForValue().get(key);

        if (json == null || json.isBlank()) {
            log.warn("Chave não encontrada no Redis: {}", key);
            return Optional.empty();
        }

        try {
            TelemetryPayloadDTO payload = objectMapper.readValue(json, TelemetryPayloadDTO.class);
            return Optional.of(payload);
        } catch (Exception e) {
            log.error("Falha ao ler JSON do Redis para o veículo {}: {}", vehicleId, e.getMessage());
            return Optional.empty();
        }
    }

    public Page<AlertEntity> getAlertsByVehicle(UUID vehicleId, Pageable pageable) {
        return alertRepository.findByVehicleIdOrderByTimestampDesc(vehicleId, pageable);
    }
}
