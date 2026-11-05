
package com.eventProApi.eventpro.dto;

import com.eventProApi.eventpro.entity.enums.StatutRemboursement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RefundResponse(
        UUID id,
        BigDecimal montant,
        String motif,
        LocalDate dateDemande,
        StatutRemboursement statut,
        UUID paymentId
) {}
