package com.autto.autto_reservation.controller;

import com.autto.autto_reservation.dto.ApiResponse;
import com.autto.autto_reservation.dto.CancelReservationInfo;
import com.autto.autto_reservation.dto.ReservationList;
import com.autto.autto_reservation.dto.ReservationRequest;
import com.autto.autto_reservation.exception.SeatNotAvailableException;
import com.autto.autto_reservation.service.ReservationService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 예약 생성
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createReservation(@RequestAttribute("userId") String userId, @RequestBody ReservationRequest reservationRequest){
        try{
            reservationService.createReservation(reservationRequest, userId);
            return ResponseEntity.ok(ApiResponse.success("예약 완료", null));
        } catch (SeatNotAvailableException e) {
            // 남은 좌석이 없는 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST) // 400 Bad Request
                    .body(ApiResponse.failure("SEAT_UNAVAILABLE", e.getMessage()));
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("EXTERNAL_API_ERROR", "API 호출 실패: " + e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("GENERAL_ERROR", "예기치 못한 오류 발생: " + e.getMessage()));
        }
    }

}
