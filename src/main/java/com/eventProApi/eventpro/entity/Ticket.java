package com.eventProApi.eventpro.entity;

import com.eventProApi.eventpro.entity.enums.StatutBillet;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {


    public Ticket create(ReservationLine reservationLine){

        Ticket ticket = new Ticket();

        ticket.setCode(UUID.randomUUID().toString());

        ticket.setDateEmission(LocalDate.now());
        ticket.setStatut(StatutBillet.VALID);
        ticket.setReservationLine(reservationLine);

        ticket.setTicketCategory(reservationLine.getTicketCategoryForEvent().getCategory());

        return ticket;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private LocalDate dateEmission;

    private LocalDate dateControle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutBillet statut = StatutBillet.VALID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_line_id", nullable = false)
    private ReservationLine reservationLine;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_category_id", nullable = false)
    private TicketCategory ticketCategory;
}