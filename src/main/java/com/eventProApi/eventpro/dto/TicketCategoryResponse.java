
package com.eventProApi.eventpro.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketCategoryResponse(
        UUID id,
        String name,
        BigDecimal prixUnitaire,
        Integer quantiteTotale,
        BigDecimal quotaMaxParClient
) {
}
