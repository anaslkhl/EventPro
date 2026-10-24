package com.eventProApi.eventpro.dto;


import com.eventProApi.eventpro.entity.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    @NotBlank(message = "First name is required !")
    @Size(max = 50, message = "First name cannot exceed 50 character")
    private String firstname;

    @NotBlank(message = "Last name is required !")
    @Size(max = 50, message = "Last name cannot exceed 50 character !")
    private String lastname;

    @NotBlank(message = "Email is required ")
    @Email(message = "Email must be valid ")
    private String email;

    @NotBlank(message = "Password is required !")
    @Size(min = 8, max = 64, message = "Password must contain between 8 and 64 characters !")
    private String password;

    @NotNull(message = "Role is required !")
    private UserRole role;
}
