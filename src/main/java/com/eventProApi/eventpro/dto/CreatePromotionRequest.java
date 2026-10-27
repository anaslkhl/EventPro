package com.eventProApi.eventpro.dto;

import com.eventProApi.eventpro.entity.enums.TypeReduction;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreatePromotionRequest(

        @NotBlank(message = "Promotion code is required")
        @Size(max = 50, message = "Code cannot exceed 50 characters")
        String code,

        @NotNull(message = "Discount type is required")
        TypeReduction typeReduction,

        @NotNull(message = "Discount value is required")
        @DecimalMin(value = "0.01", message = "Discount value must be greater than zero")
        BigDecimal valeurReduction,

        @NotNull(message = "Start date is required")
        @FutureOrPresent(message = "Start date cannot be in the past")
        LocalDate dateDebut,

        @NotNull(message = "End date is required")
        LocalDate dateFin,

        @NotNull(message = "Usage limit is required")
        @Min(value = 1, message = "Usage limit must be at least 1")
        Integer limiteUtilisation,

        @NotNull(message = "Organizer ID is required")
        UUID organizerId
) {}
