package com.autto.autto_reservation.controller;

import com.autto.autto_reservation.dto.ApiResponse;
import com.autto.autto_reservation.dto.CancelReservationInfo;
import com.autto.autto_reservation.dto.ReservationList;
import com.autto.autto_reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    // 예매 일자, 예매 번호, 예매 상태, 좌석번호
    @GetMapping("/list/{userId}")
    public ResponseEntity<ApiResponse<List<ReservationList>>> getReservationList(@PathVariable("userId") String userId){
        try {
            List<ReservationList> reservationList = reservationService.getReservationList(userId);
            return ResponseEntity.ok(ApiResponse.success("예약 전체 조회 성공", reservationList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("GENERAL_ERROR", "예기치 못한 오류 발생: " + e.getMessage()));
        }
    }

    @GetMapping("/cancel/{reservationId}")
    public ResponseEntity<ApiResponse<CancelReservationInfo>> getCancelReservation(@PathVariable("reservationId") String reservationId) {
        try {
            CancelReservationInfo cancelReservationInfo = reservationService.getCancelReservation(reservationId);
            return ResponseEntity.ok(ApiResponse.success("예약 취소 페이지 조회 성공", cancelReservationInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("GENERAL_ERROR", "예기치 못한 오류 발생: " + e.getMessage()));
        }
    }

}
