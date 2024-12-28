package com.autto.back_reservation.controller;

import com.autto.back_reservation.dto.SaveReservationDto;
import com.autto.back_reservation.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("예약 테스트 - 성공")
    public void createReservationSuccessTest() throws Exception {
        //given
        SaveReservationDto saveReservationDto = new SaveReservationDto(
                "userId123", "productId123", "seatId123", 1
        );

        doNothing().when(reservationService).createReservation(saveReservationDto);

        //when & then
        mockMvc.perform(post("/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveReservationDto)))
        .andExpect(status().isOk())
        .andExpect(content().string("예약 완료"));
    }


    @Test
    @DisplayName("예약 테스트 - 마감")
    public void createReservationFailTest() throws Exception {
        // given
        SaveReservationDto saveReservationDto = new SaveReservationDto(
                "userId123", "productId123", "seatId123", 1
        );

        doThrow(new IllegalStateException("Seats unavailable")).when(reservationService).createReservation(saveReservationDto);

        // when & then
        mockMvc.perform(post("/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveReservationDto)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("예약 마감: Seats unavailable"));
    }
}