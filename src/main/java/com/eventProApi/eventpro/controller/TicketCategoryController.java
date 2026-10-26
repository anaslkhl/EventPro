
package com.eventProApi.eventpro.controller;

import com.eventProApi.eventpro.dto.CreateTicketCategoryRequest;
import com.eventProApi.eventpro.dto.TicketCategoryResponse;
import com.eventProApi.eventpro.service.TicketCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket-categories")
@RequiredArgsConstructor
public class TicketCategoryController {

    private final TicketCategoryService ticketCategoryService;

    @PostMapping
    public ResponseEntity<TicketCategoryResponse> create(
            @Valid @RequestBody CreateTicketCategoryRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketCategoryService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<TicketCategoryResponse>> getAll() {

        return ResponseEntity.ok(
                ticketCategoryService.getAll()
        );
    }
}
