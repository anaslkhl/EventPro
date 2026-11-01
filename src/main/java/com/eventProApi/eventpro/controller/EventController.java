
package com.eventProApi.eventpro.controller;

import com.eventProApi.eventpro.dto.CreateEventRequest;
import com.eventProApi.eventpro.dto.EventResponse;
import com.eventProApi.eventpro.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;


    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody CreateEventRequest request) {

        EventResponse response = eventService.createEvent(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable UUID id) {

        return ResponseEntity.ok(eventService.getEventById(id));
    }

    // Get all events paginated

    @GetMapping
    public ResponseEntity<Page<EventResponse>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue =   "10") int size
    ) {
        return ResponseEntity.ok(eventService.getAllEvents(page, size));
    }

    // Get all events sorted with sort

    @GetMapping("/sorted")
    public ResponseEntity<List<EventResponse>> getAllEventsSorted(
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(eventService.getAllEventsSorted(sortBy, direction));
    }

    // Search with title or category or both of them

    @GetMapping("search")
    public ResponseEntity<List<EventResponse>> searchEvents(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String categoryName
    ){

        return ResponseEntity.ok( eventService.searchEvents(title, categoryName));
    }

    // Search with min price and max price

    @GetMapping("/search/price")
    public ResponseEntity<List<EventResponse>> searchByPrice(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice
    ) {
        return ResponseEntity.ok(eventService.searchByPrice(minPrice, maxPrice));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {

        eventService.deleteEvent(id);

        return ResponseEntity.noContent().build();
    }
}
