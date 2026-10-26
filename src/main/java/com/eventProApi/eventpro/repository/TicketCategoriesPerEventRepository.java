package com.eventProApi.eventpro.repository;

import com.eventProApi.eventpro.entity.TicketCategoriesPerEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketCategoriesPerEventRepository extends JpaRepository<TicketCategoriesPerEvent, UUID> {


    List<TicketCategoriesPerEvent> findByEventId(UUID id);

    boolean existsByEventIdAndCategoryId(UUID eventId, UUID categoryId);
}
