package com.autto.back_reservation.service;

import com.autto.back_reservation.entity.SeatInfo;
import com.autto.back_reservation.entity.UserReservation;
import com.autto.back_reservation.lock.DistributedLock;
import com.autto.back_reservation.dto.SaveReservationDto;
import com.autto.back_reservation.repository.ReservationRepository;
import com.autto.back_reservation.repository.SeatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatsRepository seatsRepository;

    // 자원별(좌석별) 동시성 제어
    @DistributedLock(key = "#saveReservationDto.seatId")
    public void createReservation(SaveReservationDto saveReservationDto){
        // 1. 좌석 Full 여부 체크
        UUID seatId = UUID.fromString(saveReservationDto.getSeatId());

        SeatInfo seat = seatsRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다."));

        if (seat.getStatus() == SeatInfo.SeatStatus.FULL) {
            throw new IllegalStateException("남아있는 좌석이 없습니다.");
        }

        // 2. Full 아니면 예약
        // 좌석 예약
        seat.updateSeat(saveReservationDto.getCount());
        seatsRepository.save(seat);
        // 예약 저장
        UserReservation reservation = UserReservation.fromDto(saveReservationDto, seat.getDate());
        reservationRepository.save(reservation);
    }
}
