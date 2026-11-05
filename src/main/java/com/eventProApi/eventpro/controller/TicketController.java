package com.eventProApi.eventpro.controller;

import com.eventProApi.eventpro.dto.TicketCheckInResponse;
import com.eventProApi.eventpro.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PatchMapping("/{code}/check-in")
    public ResponseEntity<TicketCheckInResponse> checkIn(
            @PathVariable String code
    ) {
        return ResponseEntity.ok(ticketService.checkIn(code));
    }
}