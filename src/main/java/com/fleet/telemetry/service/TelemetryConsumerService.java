package com.fleet.telemetry.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import com.fleet.telemetry.config.RabbitMQConfig;
import com.fleet.telemetry.domain.entity.TelemetryLogEntity;
import com.fleet.telemetry.dto.TelemetryPayloadDTO;
import com.fleet.telemetry.repository.TelemetryLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelemetryConsumerService {

    private final TelemetryLogRepository telemetryLogRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String REDIS_VEHICLE_KEY_PREFIX = "vehicle:last_position:";
    private static final String WEB_SOCKET_TOPIC = "/topic/telemetry";

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.TELEMETRY_QUEUE)
    public void consumeTelemetry(TelemetryPayloadDTO payload) {
        log.debug("Consumindo ping de telemetria -> Veículo ID: {}", payload.vehicleId());

        try {
            String redisKey = REDIS_VEHICLE_KEY_PREFIX + payload.vehicleId();
            redisTemplate.opsForValue().set(redisKey, payload, Duration.ofHours(24));

            TelemetryLogEntity telemetryLogEntity = TelemetryLogEntity.builder()
                    .vehicleId(payload.vehicleId())
                    .speed(payload.speed())
                    .latitude(payload.latitude())
                    .longitude(payload.longitude())
                    .timestamp(payload.timestamp())
                    .build();

            telemetryLogRepository.save(telemetryLogEntity);

            messagingTemplate.convertAndSend(WEB_SOCKET_TOPIC, payload);
            log.info("Processado com sucesso -> Veículo: {} | Speed: {} km/h | Salvo no Redis, PostGIS e WebSocket",
                    payload.licensePlate(), String.format("%.1f", payload.speed()));
        } catch (Exception e) {
            log.error("Erro ao processar mensagem de telemetria do veículo {}: {}", payload.vehicleId(), e.getMessage(),
                    e);
            throw e;
        }
    }
}
