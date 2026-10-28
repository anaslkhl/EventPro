
package com.eventProApi.eventpro.mapper;

import com.eventProApi.eventpro.dto.ReservationResponse;
import com.eventProApi.eventpro.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = ReservationLineMapper.class
)
public interface ReservationMapper {

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "lines", source = "reservationLines")
    ReservationResponse toResponse(Reservation reservation);
}
