
package com.eventProApi.eventpro.controller;

import com.eventProApi.eventpro.dto.CreateRefundRequest;
import com.eventProApi.eventpro.dto.RefundResponse;
import com.eventProApi.eventpro.service.RefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    public ResponseEntity<RefundResponse> requestRefund(
            @Valid @RequestBody CreateRefundRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(refundService.requestRefund(request));
    }

    @PatchMapping("/{refundId}/approve")
    public ResponseEntity<RefundResponse> approveRefund(
            @PathVariable UUID refundId) {

        return ResponseEntity.ok(
                refundService.approveRefund(refundId)
        );
    }

    @PatchMapping("/{refundId}/reject")
    public ResponseEntity<RefundResponse> rejectRefund(
            @PathVariable UUID refundId) {

        return ResponseEntity.ok(
                refundService.rejectRefund(refundId)
        );
    }
}
