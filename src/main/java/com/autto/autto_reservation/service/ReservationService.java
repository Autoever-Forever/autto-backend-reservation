package com.autto.autto_reservation.service;

import com.autto.autto_reservation.client.ReservationServiceFeignClient;
import com.autto.autto_reservation.dto.*;
import com.autto.autto_reservation.entity.UserReservation;
import com.autto.autto_reservation.exception.SeatNotAvailableException;
import com.autto.autto_reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationServiceFeignClient productApi;

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

    @Transactional
    public void createReservation(ReservationRequest reservationRequest){

        // 예약하기
        UserReservation reservation = new UserReservation(reservationRequest);
        reservationRepository.save(reservation);

        // 재고 업데이트(API)
        InventoryValidationRequest checkInventory = new InventoryValidationRequest(reservationRequest.getSeatId(), reservationRequest.getSeatCount());

        InventoryValidationResponse updateResponse = productApi.updateSeats(checkInventory);
        if(!updateResponse.getSuccess()){
            throw new SeatNotAvailableException("좌석 수 업데이트에 실패하였습니다: " + updateResponse);
        }
    }

}
