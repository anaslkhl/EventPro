package com.eventProApi.eventpro.dto;

import com.eventProApi.eventpro.entity.enums.StatutBillet;

import java.time.LocalDate;
import java.util.UUID;

public record TicketCheckInResponse(
        UUID ticketId,
        String code,
        StatutBillet statut,
        LocalDate dateControle
) {}