package com.eventProApi.eventpro.service;

import com.eventProApi.eventpro.dto.CreateUserRequest;
import com.eventProApi.eventpro.dto.UserResponse;
import com.eventProApi.eventpro.entity.User;
import com.eventProApi.eventpro.mapper.UserMapper;
import com.eventProApi.eventpro.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(CreateUserRequest request){

        String email = normalizeEmail(request.getEmail());

        if(userRepository.existsByEmail(email)){
            throw new IllegalArgumentException("A user with this email already exist !");
        }

        String encodePassword = passwordEncoder.encode(request.getPassword());

        User user = userMapper.toEntity(request, encodePassword);

        User savedUser = userRepository.save(user);


        return userMapper.toResponse(savedUser);

    }



    public UserResponse getUserById(UUID id){

        User user = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NoSuchElementException("Active user not found !"));

        return userMapper.toResponse(user);
    }


    public UserResponse getUserByEmail(String email){

        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("Email must not be empty !");
        }

        User user = userRepository.findByEmail(normalizeEmail(email))
                .filter(User::isActive).orElseThrow(() -> new NoSuchElementException("Active user not found !"));

        return userMapper.toResponse(user);
    }

    public List<UserResponse> getAllUsers(){

        return userRepository.findAllByActiveTrue().stream().map(userMapper::toResponse).toList();
    }



    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}


