
package com.eventProApi.eventpro.mapper;

import com.eventProApi.eventpro.dto.CreatePromotionRequest;
import com.eventProApi.eventpro.dto.PromotionResponse;
import com.eventProApi.eventpro.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "nombreUtilisations", ignore = true)
    Promotion toEntity(CreatePromotionRequest request);

    @Mapping(target = "organizerId", source = "organizer.id")
    PromotionResponse toResponse(Promotion promotion);

    List<PromotionResponse> toResponseList(List<Promotion> promotions);

}
