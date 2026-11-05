
package com.eventProApi.eventpro.dto;

import com.eventProApi.eventpro.entity.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;

public record ProcessPaymentRequest(

        @NotNull(message = "Payment result is required")
        PaymentStatus result

) {}
