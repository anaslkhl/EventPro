
package com.eventProApi.eventpro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateRefundRequest(

        @NotNull
        UUID paymentId,

        @NotBlank
        @Size(max = 500)
        String motif

) {}
