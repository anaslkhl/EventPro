
package com.eventProApi.eventpro.controller;

import com.eventProApi.eventpro.dto.CreateReservationRequest;
import com.eventProApi.eventpro.dto.ReservationResponse;
import com.eventProApi.eventpro.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
           @Valid @RequestBody CreateReservationRequest request) {

        ReservationResponse response =
                reservationService.createReservation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservationById(
            @PathVariable UUID reservationId) {

        return ResponseEntity.ok(
                reservationService.getReservationById(reservationId)
        );
    }


    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<ReservationResponse> cancelReservation(
            @PathVariable UUID reservationId) {

        return ResponseEntity.ok(
                reservationService.cancelReservation(reservationId)
        );
    }

}
