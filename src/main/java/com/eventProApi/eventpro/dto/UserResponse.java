package com.eventProApi.eventpro.dto;


import com.eventProApi.eventpro.entity.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder

public class UserResponse {


    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private UserRole role;

    private boolean active;

    private String organizationName;
    private String phoneNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
