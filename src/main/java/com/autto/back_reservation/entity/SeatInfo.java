package com.autto.back_reservation.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class SeatInfo {

    @Id
    private UUID  id;

    private UUID productId;

    private Date date;

    private int reservedSeats;

    private int totalSeats;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    public enum SeatStatus {
        FULL, AVAILABLE
    }

    public void updateSeat(int count) {
        validateSeatCount(count);
        this.reservedSeats += count;

        if(this.reservedSeats >= totalSeats){
            this.status = SeatStatus.FULL;
        }
    }

    public void validateSeatCount(int count){
        int remainSeats = totalSeats - reservedSeats;
        if (remainSeats < count){
            throw new IllegalStateException("남아있는 좌석이 없습니다.");
        }
    }
}
