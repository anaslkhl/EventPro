package com.eventProApi.eventpro.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ClientResponse extends UserResponse {

    private String phoneNumber;
}