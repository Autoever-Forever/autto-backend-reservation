package com.autto.autto_reservation.dto;

import com.autto.autto_reservation.entity.UserReservation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class CancelReservationInfo {
    private UUID seatId; //좌석 번호
    private int seatCount;  //예약 좌석 수
    private UserReservation.ReservationStatus ReservationStatus;; // 예약 상태
}
