
package com.eventProApi.eventpro.service;

import com.eventProApi.eventpro.dto.CreateUserRequest;
import com.eventProApi.eventpro.dto.UserResponse;
import com.eventProApi.eventpro.entity.*;
import com.eventProApi.eventpro.mapper.UserMapper;
import com.eventProApi.eventpro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final OrganizerRepository organizerRepository;
    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;
    private final AgentControleRepository agentControleRepository;
    private final UserMapper userMapper;

    public UserResponse createUser(CreateUserRequest request) {

        String email = normalizeEmail(request.getEmail());

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "A user with this email already exists!"
            );
        }


        User savedUser = switch (request.getRole()) {

            case USER -> userRepository.save(userMapper.toUserEntity(request));

            case ORGANIZER ->organizerRepository.save(userMapper.toOrganizerEntity(request));

            case CLIENT ->clientRepository.save(userMapper.toClientEntity(request));

            case ADMIN -> adminRepository.save(userMapper.toAdminEntity(request));

            case AGENT_CONTROLE -> agentControleRepository.save(userMapper.toAgentControleEntity(request));
        };

        return userMapper.toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found with id: " + id
                        )
                );

        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new NoSuchElementException(
                        "User not found with email: " + email
                ));

        return userMapper.toResponse(user);
    }

}
