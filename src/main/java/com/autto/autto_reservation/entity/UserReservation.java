package com.autto.autto_reservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private LocalDateTime createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastUpdate;

    @PrePersist
    public void prePersist(){
        createdDate = LocalDateTime.now();
        lastUpdate = LocalDateTime.now();

        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdate = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    // 예약, 예약취소
    public enum ReservationStatus {
        ACTIVE,
        INACTIVE
    }
}
