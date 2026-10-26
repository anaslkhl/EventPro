
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
        String name

) {
}
