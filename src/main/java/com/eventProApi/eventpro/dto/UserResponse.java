package com.eventProApi.eventpro.dto;


import com.eventProApi.eventpro.entity.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder

public class UserResponse {


    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private UserRole role;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
