
package com.eventProApi.eventpro.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateTicketCategoryRequest(

        @NotBlank(message = "Category name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,

        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
        BigDecimal prixUnitaire,

        @NotNull(message = "Total quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantiteTotale,

        @NotNull(message = "Maximum quota per client is required")
        @DecimalMin(value = "1", message = "Quota must be at least 1")
        BigDecimal quotaMaxParClient

) {
}
