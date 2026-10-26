package com.eventProApi.eventpro.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
@Getter
@SuperBuilder

public class OrganizerResponse extends UserResponse {

    private String organizationName;
}
