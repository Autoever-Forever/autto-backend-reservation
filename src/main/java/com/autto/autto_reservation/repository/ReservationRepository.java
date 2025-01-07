package com.autto.autto_reservation.repository;

import com.autto.autto_reservation.dto.CancelReservationInfo;
import com.autto.autto_reservation.dto.ReservationList;
import com.autto.autto_reservation.entity.UserReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<UserReservation, UUID> {

    @Query("SELECT new com.autto.autto_reservation.dto.ReservationList(r.id, r.createdDate, r.status, r.seatId) " +
            "FROM UserReservation r WHERE r.userId = :userId")
    List<ReservationList> findReservationsByUserId(@Param("userId") UUID userId);

    @Query("SELECT new com.autto.autto_reservation.dto.CancelReservationInfo(r.seatId, r.seatCount) " +
            "FROM UserReservation r WHERE r.id = :reservationId")
    CancelReservationInfo findReservationById(@Param("reservationId") UUID reservationId);
}




