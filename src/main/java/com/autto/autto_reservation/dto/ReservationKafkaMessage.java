package com.autto.autto_reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class ReservationKafkaMessage {
    private UUID productId;
    private int seatCount;
}
