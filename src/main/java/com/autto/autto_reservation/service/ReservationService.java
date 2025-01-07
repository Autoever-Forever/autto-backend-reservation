package com.autto.autto_reservation.service;

import com.autto.autto_reservation.dto.CancelReservationInfo;
import com.autto.autto_reservation.dto.ReservationList;
import com.autto.autto_reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    // 사용자 예약 조회 API
    public List<ReservationList> getReservationList(String userId) throws Exception {
        // 사용자 ID로 여러 예약을 조회
        UUID userUUID = UUID.fromString(userId);
        List<ReservationList> reservations = reservationRepository.findReservationsByUserId(userUUID);
        return reservations;
    }

    // 사용자 예약 취소 상세 조회 API
    public CancelReservationInfo getCancelReservation(String reservationId) {
        UUID reservationUUID = UUID.fromString(reservationId);
        CancelReservationInfo cancelReservation = reservationRepository.findReservationById(reservationUUID);
        return cancelReservation;
    }

}
