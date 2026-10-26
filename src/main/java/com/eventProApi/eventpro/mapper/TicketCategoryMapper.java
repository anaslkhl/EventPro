
package com.eventProApi.eventpro.mapper;

import com.eventProApi.eventpro.dto.CreateTicketCategoryRequest;
import com.eventProApi.eventpro.dto.TicketCategoryResponse;
import com.eventProApi.eventpro.entity.TicketCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketCategoryMapper {

    @Mapping(target = "id", ignore = true)
    TicketCategory toEntity(CreateTicketCategoryRequest request);

    TicketCategoryResponse toResponse(TicketCategory category);

    List<TicketCategoryResponse> toResponseList(
            List<TicketCategory> categories
    );
}
