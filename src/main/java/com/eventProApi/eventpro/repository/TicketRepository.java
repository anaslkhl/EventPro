package com.eventProApi.eventpro.repository;

import com.eventProApi.eventpro.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    List<Ticket> findByReservationLineId(UUID reservationLineId);
}