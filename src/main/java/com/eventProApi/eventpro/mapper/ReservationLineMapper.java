
package com.eventProApi.eventpro.mapper;
import com.eventProApi.eventpro.dto.ReservationLineResponse;
import com.eventProApi.eventpro.entity.ReservationLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationLineMapper {

    @Mapping(
            target = "ticketCategoryAssignmentId",
            source = "ticketCategoryForEvent.id"
    )
    @Mapping(
            target = "categoryName",
            source = "ticketCategoryForEvent.category.name"
    )
    @Mapping(
            target = "eventTitle",
            source = "ticketCategoryForEvent.event.title"
    )
    @Mapping(target = "quantity", source = "quantite")
    @Mapping(target = "unitPrice", source = "prixUnitaire")
    @Mapping(target = "subtotal", source = "sousTotal")
    ReservationLineResponse toResponse(ReservationLine reservationLine);
}
