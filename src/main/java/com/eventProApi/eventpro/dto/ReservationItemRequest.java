package com.eventProApi.eventpro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ReservationItemRequest(

        @NotNull
        UUID ticketCategoryAssignmentId,

        @NotNull
        @Positive
        Integer quantity

) {}
