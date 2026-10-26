package com.eventProApi.eventpro.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@SuperBuilder
public class ClientResponse extends UserResponse {

    private String phoneNumber;
}