package com.autto.autto_reservation.dto;

import lombok.Data;

@Data
public class ReservationRequest {
    String userId;
    String productId;
    String seatId;
    int seatCount;
}
