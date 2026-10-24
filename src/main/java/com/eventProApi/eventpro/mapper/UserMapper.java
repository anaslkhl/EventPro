package com.eventProApi.eventpro.mapper;


import com.eventProApi.eventpro.dto.CreateUserRequest;
import com.eventProApi.eventpro.dto.UserResponse;
import com.eventProApi.eventpro.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request, String encodedPassword){

        return User.builder()

                .firstName(request.getFirstname().trim())
                .lastName(request.getFirstname().trim())
                .email(request.getEmail().trim())
                .active(true)
                .build();
    }


    public UserResponse toResponse(User user){

        return  UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
