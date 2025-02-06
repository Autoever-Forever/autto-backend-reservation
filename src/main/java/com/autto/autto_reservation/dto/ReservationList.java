package com.autto.autto_reservation.dto;//package com.autto.b_reservation.dto;

import com.autto.autto_reservation.entity.UserReservation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Data
public class ReservationList {

    private UUID reservationId;  // 예약 번호
    private LocalDateTime createdDate;      // 예약 일자
    private UserReservation.ReservationStatus status;  // 예약 상태
    private UUID seatId;         // 좌석 번호
}
