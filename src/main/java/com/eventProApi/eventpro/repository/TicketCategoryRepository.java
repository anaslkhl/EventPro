
package com.eventProApi.eventpro.repository;

import com.eventProApi.eventpro.entity.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketCategoryRepository
        extends JpaRepository<TicketCategory, UUID> {
}
