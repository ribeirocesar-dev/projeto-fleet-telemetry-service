package com.fleet.telemetry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FleetTelemetryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FleetTelemetryServiceApplication.class, args);
	}

}
