package com.autto.back_reservation.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaveReservationDto {
    String userId;
    String productId;
    String seatId;
    int count;
}
