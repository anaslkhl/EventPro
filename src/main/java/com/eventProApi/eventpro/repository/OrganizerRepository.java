package com.eventProApi.eventpro.repository;

import com.eventProApi.eventpro.entity.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OrganizerRepository
        extends JpaRepository<Organizer, UUID> {
}