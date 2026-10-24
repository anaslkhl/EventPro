package com.eventProApi.eventpro.repository;

import com.eventProApi.eventpro.entity.AgentControle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AgentControleRepository
        extends JpaRepository<AgentControle, UUID> {
}