package com.eventProApi.eventpro.repository;

import com.eventProApi.eventpro.dto.EventResponse;
import com.eventProApi.eventpro.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID>,
        JpaSpecificationExecutor<Event> {


    List<Event> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    Event findBy(UUID id);
}
