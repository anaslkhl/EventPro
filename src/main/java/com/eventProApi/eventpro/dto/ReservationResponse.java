package com.eventProApi.eventpro.dto;
import com.eventProApi.eventpro.entity.enums.StatutReservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReservationResponse(

        UUID id,

        String reference,

        LocalDateTime dateCreation,

        LocalDateTime dateExpiration,

        StatutReservation statut,

        BigDecimal montantTotal,

        UUID clientId,

        List<ReservationLineResponse> lines

) {}
