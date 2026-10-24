package com.eventProApi.eventpro.service;

import com.eventProApi.eventpro.repository.AgentControleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AgentControleService {

    private final AgentControleRepository agentControleRepository;
    private final UserService userService;

}