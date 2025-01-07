package com.autto.autto_reservation.client;

import com.autto.autto_reservation.dto.InventoryValidationRequest;
import com.autto.autto_reservation.dto.InventoryValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "reservationClient", url = "${reservation.service.url}")
public interface ReservationServiceFeignClient {

    // 재고 업데이트 API
    @GetMapping("/api/v1/seats/validate")
    InventoryValidationResponse updateSeats(@RequestBody InventoryValidationRequest checkInventory);

}