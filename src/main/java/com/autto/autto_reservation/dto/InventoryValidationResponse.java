package com.autto.autto_reservation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryValidationResponse {
    private boolean success;
    private String message;
    private ReservationResult result;

    @Getter
    @Builder
    public static class ReservationResult {
        private Integer remainingSeats;
        private Integer reservedSeats;
    }

    public boolean getSuccess() {
        return success;
    }
}