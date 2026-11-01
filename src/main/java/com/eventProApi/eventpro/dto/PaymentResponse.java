package com.eventProApi.eventpro.dto;

import com.eventProApi.eventpro.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        String reference,
        BigDecimal montant,
        PaymentStatus statut,
        LocalDateTime dateCreation,
        LocalDateTime datePaiement,
        UUID reservationId
) {}