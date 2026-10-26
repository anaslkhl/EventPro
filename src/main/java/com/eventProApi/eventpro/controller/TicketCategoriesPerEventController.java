
package com.eventProApi.eventpro.controller;

import com.eventProApi.eventpro.dto.CreateTicketCategoriesPerEventRequest;
import com.eventProApi.eventpro.dto.TicketCategoriesPerEventResponse;
import com.eventProApi.eventpro.service.TicketCategoriesPerEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events/{eventId}/ticket-categories")
@RequiredArgsConstructor
public class TicketCategoriesPerEventController {

    private final TicketCategoriesPerEventService ticketCategoriesPerEventService;

    @PostMapping
    public ResponseEntity<TicketCategoriesPerEventResponse> create(
            @PathVariable UUID eventId,
            @Valid @RequestBody CreateTicketCategoriesPerEventRequest request
    ) {
        TicketCategoriesPerEventResponse response =
                ticketCategoriesPerEventService.create(eventId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TicketCategoriesPerEventResponse>> getByEventId(
            @PathVariable UUID eventId
    ) {
        return ResponseEntity.ok(
                ticketCategoriesPerEventService.getByEventId(eventId)
        );
    }
}
