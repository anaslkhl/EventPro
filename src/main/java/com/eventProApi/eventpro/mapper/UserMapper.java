
package com.eventProApi.eventpro.mapper;

import com.eventProApi.eventpro.dto.CreateUserRequest;
import com.eventProApi.eventpro.dto.UserResponse;
import com.eventProApi.eventpro.entity.*;
import com.eventProApi.eventpro.entity.enums.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Locale;

@Mapper(
        componentModel = "spring",
        imports = {
                Locale.class,
                UserRole.class
        }
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", source = "firstname")
    @Mapping(target = "lastName", source = "lastname")
    @Mapping(
            target = "email",
            expression = "java(request.getEmail().trim().toLowerCase(Locale.ROOT))"
    )


    @Mapping(target = "role", expression = "java(UserRole.USER)")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUserEntity(CreateUserRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", source = "firstname")
    @Mapping(target = "lastName", source = "lastname")
    @Mapping(
            target = "email",
            expression = "java(request.getEmail().trim().toLowerCase(Locale.ROOT))")
    @Mapping(target = "role", expression = "java(UserRole.ORGANIZER)")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Organizer toOrganizerEntity(CreateUserRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", source = "firstname")
    @Mapping(target = "lastName", source = "lastname")
    @Mapping(
            target = "email",
            expression = "java(request.getEmail().trim().toLowerCase(Locale.ROOT))"
    )
    @Mapping(target = "role", expression = "java(UserRole.CLIENT)")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Client toClientEntity(CreateUserRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", source = "firstname")
    @Mapping(target = "lastName", source = "lastname")
    @Mapping(
            target = "email",
            expression = "java(request.getEmail().trim().toLowerCase(Locale.ROOT))"
    )
    @Mapping(target = "role", expression = "java(UserRole.ADMIN)")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Admin toAdminEntity(CreateUserRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", source = "firstname")
    @Mapping(target = "lastName", source = "lastname")
    @Mapping(
            target = "email",
            expression = "java(request.getEmail().trim().toLowerCase(Locale.ROOT))"
    )
    @Mapping(target = "role", expression = "java(UserRole.AGENT_CONTROLE)")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AgentControle toAgentControleEntity(CreateUserRequest request);

    @Mapping(target = "organizationName", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    UserResponse toBaseResponse(User user);

    @Mapping(target = "organizationName", source = "organizationName")
    @Mapping(target = "phoneNumber", ignore = true)
    UserResponse toOrganizerResponse(Organizer organizer);

    @Mapping(target = "organizationName", ignore = true)
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    UserResponse toClientResponse(Client client);

    default UserResponse toResponse(User user) {
        if (user instanceof Organizer organizer) {
            return toOrganizerResponse(organizer);
        }

        if (user instanceof Client client) {
            return toClientResponse(client);
        }

        return toBaseResponse(user);
    }
}
