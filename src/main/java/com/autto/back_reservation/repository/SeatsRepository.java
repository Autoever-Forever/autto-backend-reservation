package com.autto.back_reservation.repository;

import com.autto.back_reservation.entity.SeatInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeatsRepository extends JpaRepository<SeatInfo, UUID> {
}
