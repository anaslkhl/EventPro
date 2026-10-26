
package com.eventProApi.eventpro.mapper;

import com.eventProApi.eventpro.dto.CreateTicketCategoriesPerEventRequest;
import com.eventProApi.eventpro.dto.TicketCategoriesPerEventResponse;
import com.eventProApi.eventpro.entity.TicketCategoriesPerEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketCategoriesPerEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "category", ignore = true)
    TicketCategoriesPerEvent toEntity(
            CreateTicketCategoriesPerEventRequest request
    );

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    TicketCategoriesPerEventResponse toResponse(
            TicketCategoriesPerEvent entity
    );
}
