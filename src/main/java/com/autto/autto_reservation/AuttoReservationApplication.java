package com.autto.autto_reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AuttoReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuttoReservationApplication.class, args);
	}

}
