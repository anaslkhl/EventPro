
package com.eventProApi.eventpro.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTicketCategoriesPerEventRequest(

        @NotNull(message = "Category ID is required")
        UUID categoryId,

        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
        BigDecimal unitPrice,

        @NotNull(message = "Total quantity is required")
        @Min(value = 1, message = "Total quantity must be at least 1")
        Integer totalQuantity,

        @NotNull(message = "Maximum quota per client is required")
        @Min(value = 1, message = "Maximum quota per client must be at least 1")
        Integer maxQuotaPerClient

) {}
