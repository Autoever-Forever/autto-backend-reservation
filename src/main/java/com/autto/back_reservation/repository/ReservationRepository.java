package com.autto.back_reservation.repository;

import com.autto.back_reservation.entity.UserReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<UserReservation, UUID> {
}
