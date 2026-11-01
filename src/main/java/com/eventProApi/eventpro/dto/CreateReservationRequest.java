package com.eventProApi.eventpro.dto;
import com.eventProApi.eventpro.entity.ReservationLine;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateReservationRequest(

        @NotNull(message = "Client ID is required")
        UUID clientId,

        @NotNull(message = "Event ID is required")
        UUID eventId,

        @NotEmpty(message = "A reservation must contain at least one line")
        List<@NotNull @Valid ReservationLineRequest> lines

) {}
