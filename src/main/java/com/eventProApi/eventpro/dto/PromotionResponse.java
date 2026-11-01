
package com.eventProApi.eventpro.dto;

import com.eventProApi.eventpro.entity.enums.TypeReduction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PromotionResponse(
        UUID id,
        String code,
        TypeReduction typeReduction,
        BigDecimal valeurReduction,
        LocalDate dateDebut,
        LocalDate dateFin,
        Integer limiteUtilisation,
        Long nombreUtilisations,
        UUID organizerId
) {}
