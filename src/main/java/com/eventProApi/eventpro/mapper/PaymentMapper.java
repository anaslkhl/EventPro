package com.eventProApi.eventpro.mapper;

import com.eventProApi.eventpro.dto.PaymentResponse;
import com.eventProApi.eventpro.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "reservation.id", target = "reservationId")
    PaymentResponse toResponse(Payment payment);

}