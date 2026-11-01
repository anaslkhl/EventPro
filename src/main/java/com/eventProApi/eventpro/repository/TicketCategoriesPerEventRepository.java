package com.eventProApi.eventpro.repository;

import com.eventProApi.eventpro.entity.TicketCategoriesPerEvent;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketCategoriesPerEventRepository extends JpaRepository<TicketCategoriesPerEvent, UUID> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT tc
    FROM TicketCategoriesPerEvent tc
    WHERE tc.id = :id
    """)
    Optional<TicketCategoriesPerEvent> findByIdForUpdate(
            @Param("id") UUID id
    );

    List<TicketCategoriesPerEvent> findByEventId(UUID id);

    boolean existsByEventIdAndCategoryId(UUID eventId, UUID categoryId);
}
