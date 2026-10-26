package com.eventProApi.eventpro.dto;


import com.eventProApi.eventpro.entity.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor

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

    @NotNull(message = "Role is required !")
    private UserRole role;

    @Size(max = 150)
    private String organizationName;

    @Size(max = 30)
    private String phoneNumber;

    @AssertTrue(message = "Organization name is required for an organizer!")

    public boolean isOrganizationNameValid() {
        return role != UserRole.ORGANIZER || (organizationName != null && !organizationName.isBlank());
    }

    @AssertTrue(message = "Phone number is required for a client!")
    public boolean isPhoneNumberValid() {
        return role != UserRole.CLIENT || (phoneNumber != null && !phoneNumber.isBlank());
    }

}
