package com.autto.back_reservation.controller;

import com.autto.back_reservation.dto.ApiResponse;
import com.autto.back_reservation.service.ReservationService;
import com.autto.back_reservation.dto.SaveReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/reservation")
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> saveReservation(@RequestBody SaveReservationDto saveReservationDto){
        try {
            // 예약 처리
            reservationService.createReservation(saveReservationDto);
            return ResponseEntity.ok(ApiResponse.success("예약 완료", null));
        } catch (IllegalStateException e) {
            // 남은 좌석이 없는 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST) // 400 Bad Request
                    .body(ApiResponse.failure("SEAT_UNAVAILABLE", "예약 마감"));
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("GENERAL_ERROR", "예약 오류"));
        }
    }
}
