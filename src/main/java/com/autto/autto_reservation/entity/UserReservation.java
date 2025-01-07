package com.autto.autto_reservation.entity;

import com.autto.autto_reservation.dto.ReservationRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserReservation {

    @Id
    private UUID id;

    private UUID userId;

    private UUID productId;

    private UUID seatId;

    private int seatCount;


    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    @PrePersist
    public void prePersist(){
        createdDate = new Date();
        lastUpdate = new Date();

        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdate = new Date();
    }

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    // 예약, 예약취소
    public enum ReservationStatus {
        ACTIVE,
        INACTIVE
    }

    // dto to Entity
    public UserReservation(ReservationRequest reservationRequest) {
        this.userId = UUID.fromString(reservationRequest.getUserId());
        this.productId = UUID.fromString(reservationRequest.getProductId());
        this.seatId = UUID.fromString(reservationRequest.getSeatId());
        this.seatCount = reservationRequest.getSeatCount();
        this.status = ReservationStatus.ACTIVE;
    }

    // 상태 변경 메서드
    public void changeStatusInactive() {
        this.status = ReservationStatus.INACTIVE;
    }
}
