package com.eventProApi.eventpro.service;

import com.eventProApi.eventpro.repository.OrganizerRepository;
import com.eventProApi.eventpro.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizerService {

    private final OrganizerRepository organizerRepository;
    private final UserService userService;

}