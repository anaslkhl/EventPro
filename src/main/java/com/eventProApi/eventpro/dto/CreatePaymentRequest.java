package com.eventProApi.eventpro.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreatePaymentRequest(

        @NotNull(message = "Reservation ID is required")
        UUID reservationId

) {}