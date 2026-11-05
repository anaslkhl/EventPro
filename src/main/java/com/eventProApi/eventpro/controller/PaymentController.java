
package com.eventProApi.eventpro.controller;

import com.eventProApi.eventpro.dto.CreatePaymentRequest;
import com.eventProApi.eventpro.dto.PaymentResponse;
import com.eventProApi.eventpro.dto.ProcessPaymentRequest;
import com.eventProApi.eventpro.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request) {

        PaymentResponse response = paymentService.createPayment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/{paymentId}/process")
    public ResponseEntity<PaymentResponse> processPayment(
            @PathVariable UUID paymentId,
            @Valid @RequestBody ProcessPaymentRequest request) {

        PaymentResponse response = paymentService.processPayment(
                paymentId,
                request.result()
        );

        return ResponseEntity.ok(response);
    }
}
