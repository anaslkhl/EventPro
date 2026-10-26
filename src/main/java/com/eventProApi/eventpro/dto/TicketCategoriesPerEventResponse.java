
package com.eventProApi.eventpro.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketCategoriesPerEventResponse(
        UUID id,
        UUID eventId,
        UUID categoryId,
        String categoryName,
        BigDecimal unitPrice,
        Integer totalQuantity,
        Integer maxQuotaPerClient
) {}
