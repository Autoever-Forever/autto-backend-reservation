package com.autto.back_reservation.entity;

import com.autto.back_reservation.dto.SaveReservationDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserReservation {

    @Id
    private UUID  id;

    private UUID  userId;

    private UUID  productId;

    private Date ticketDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public enum ReservationStatus {
        BOOKED, CANCELED
    }

    // DTO to Entity 변환 메서드
    public static UserReservation fromDto(SaveReservationDto dto, Date date) {
        UserReservation reservation = new UserReservation();
        reservation.id = UUID.randomUUID(); // ID 자동 생성
        reservation.userId = UUID.fromString(dto.getUserId());
        reservation.productId = UUID.fromString(dto.getProductId());
        reservation.ticketDate = date;
        reservation.createdDate = new Date(); // 현재 시간
        reservation.lastUpdate = new Date(); // 초기 생성 시간과 동일
        reservation.status = ReservationStatus.BOOKED; // 기본 상태 설정
        return reservation;
    }
}
