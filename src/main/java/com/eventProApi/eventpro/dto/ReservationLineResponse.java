package com.eventProApi.eventpro.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ReservationLineResponse(

        UUID id,

        UUID ticketCategoryAssignmentId,

        String categoryName,

        String eventTitle,

        Integer quantity,

        BigDecimal unitPrice,

        BigDecimal subtotal

) {}
