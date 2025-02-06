package com.autto.autto_reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class InventoryValidationRequest {
    private String inventoryId;
    private Integer quantity;
}
