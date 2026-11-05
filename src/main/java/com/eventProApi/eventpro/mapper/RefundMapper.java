
package com.eventProApi.eventpro.mapper;

import com.eventProApi.eventpro.dto.RefundResponse;
import com.eventProApi.eventpro.entity.Refund;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RefundMapper {

    @Mapping(source = "payment.id", target = "paymentId")
    RefundResponse toResponse(Refund refund);
}
