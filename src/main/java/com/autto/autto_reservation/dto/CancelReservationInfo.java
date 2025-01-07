package com.autto.autto_reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class CancelReservationInfo {
    private UUID seatId; //좌석 번호
    private int seatCount;  //예약 좌석 수
}
