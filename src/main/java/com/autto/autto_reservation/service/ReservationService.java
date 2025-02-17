package com.autto.autto_reservation.service;

import com.autto.autto_reservation.client.ReservationServiceFeignClient;
import com.autto.autto_reservation.dto.*;
import com.autto.autto_reservation.entity.UserReservation;
import com.autto.autto_reservation.exception.SeatNotAvailableException;
import com.autto.autto_reservation.repository.ReservationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationServiceFeignClient productApi;

    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${kafka.topic.name}")
    private String topic;

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
    public UserReservation createReservation(ReservationRequest reservationRequest, String userId){
        // 예약하기
        reservationRequest.setUserId(userId);
        UserReservation reservation = new UserReservation(reservationRequest);
        UserReservation userReservation = reservationRepository.save(reservation);

        // 재고 업데이트(API)
//        InventoryValidationRequest checkInventory = new InventoryValidationRequest(reservationRequest.getSeatId(), reservationRequest.getSeatCount());
//
//        InventoryValidationResponse updateResponse = productApi.updateSeats(checkInventory);
//        if(!updateResponse.getSuccess()){
//            throw new SeatNotAvailableException("좌석 수 업데이트에 실패하였습니다: " + updateResponse);
//        }

        return userReservation;
    }

    //예약 취소
    @Transactional
    public void cancelReservation(String reservationId) throws Exception {
        UserReservation reservation = reservationRepository.findById(UUID.fromString(reservationId))
                .orElseThrow(() -> new Exception("예약을 찾을 수 없습니다: " + reservationId));

        // 예약취소
        reservation.changeStatusInactive();
        reservationRepository.save(reservation);

        // Kafka Producer를 통해 메시지 발행
        sendReservationCancelMessage(topic, reservation);
        log.info("메시지 발송 성공");
    }

    // 메시지 발행
    public void sendReservationCancelMessage(String topic, UserReservation reservation) throws JsonProcessingException {
            ReservationKafkaMessage message = new ReservationKafkaMessage(
                    reservation.getSeatId(),
                    reservation.getSeatCount()
            );

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(message);

            kafkaTemplate.send(topic, jsonMessage);
    }
}
