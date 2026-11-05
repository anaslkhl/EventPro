package com.eventProApi.eventpro.service;

import com.eventProApi.eventpro.dto.TicketCheckInResponse;
import com.eventProApi.eventpro.entity.Ticket;
import com.eventProApi.eventpro.entity.enums.StatutBillet;
import com.eventProApi.eventpro.exception.BusinessException;
import com.eventProApi.eventpro.exception.ResourceNotFoundException;
import com.eventProApi.eventpro.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    @Transactional
    public TicketCheckInResponse checkIn(String code) {

        Ticket ticket = ticketRepository.findByCodeForUpdate(code)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ticket not found")
                );

        if (ticket.getStatut() == StatutBillet.USED) {
            throw new BusinessException("This ticket has already been used");
        }

        if (ticket.getStatut() == StatutBillet.CANCELLED) {
            throw new BusinessException("This ticket has been cancelled");
        }

        ticket.setStatut(StatutBillet.USED);
        ticket.setDateControle(LocalDate.now());

        return new TicketCheckInResponse(
                ticket.getId(),
                ticket.getCode(),
                ticket.getStatut(),
                ticket.getDateControle()
        );
    }
}