
package com.eventProApi.eventpro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class CreateOrganizerRequest {

    @NotBlank
    @Size(max = 150)
    private String organizationName;
}
