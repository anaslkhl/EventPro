package com.eventProApi.eventpro.service;

import com.eventProApi.eventpro.dto.CreateReservationRequest;
import com.eventProApi.eventpro.dto.ReservationLineRequest;
import com.eventProApi.eventpro.dto.ReservationResponse;
import com.eventProApi.eventpro.entity.*;
import com.eventProApi.eventpro.entity.enums.StatutReservation;
import com.eventProApi.eventpro.exception.BusinessException;
import com.eventProApi.eventpro.exception.ResourceNotFoundException;
import com.eventProApi.eventpro.mapper.ReservationMapper;
import com.eventProApi.eventpro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ReservationService {


    private final ClientRepository clientRepository;
    private final EventRepository eventRepository;
    private final ReservationRepository reservationRepository;
    private final TicketCategoriesPerEventRepository ticketCategoryRepository;
    private final ReservationMapper reservationMapper;

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest reservationRequest){

        Client client = clientRepository.findById(reservationRequest.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("There is no client !" + reservationRequest.clientId()));


        if(!client.isActive()){
            throw new BusinessException("This client account is unactive !");
        }


        if(reservationRequest.lines() == null || reservationRequest.lines().isEmpty()){

            throw new ResourceNotFoundException("A reservation must contain at least one line");
        }

        for(ReservationLineRequest line : reservationRequest.lines()){

            if(line.quantity() == null || line.quantity() <= 0){
                throw new BusinessException("Reservation quantity must be greater than zero");
            }
        }



        Event event = eventRepository.findById(reservationRequest.eventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found" + reservationRequest.eventId()));



        if(!event.isPublished()){
            throw new BusinessException("This event is not available for reservations");
        }

        LocalDateTime now = LocalDateTime.now();

        if (!now.isBefore(event.getStartDate())) {
            throw new BusinessException(
                    "Reservations are closed because this event has already started"
            );
        }

        Map<UUID, Integer> quantitiesByAssignment = new LinkedHashMap<>();

        for (ReservationLineRequest line : reservationRequest.lines()) {
            quantitiesByAssignment.merge(
                    line.ticketCategoriesPerEventId(),
                    line.quantity(),
                    Integer::sum
            );
        }


        Map<UUID, TicketCategoriesPerEvent> assignments = new LinkedHashMap<>();

        for (Map.Entry<UUID, Integer> entry : quantitiesByAssignment.entrySet()) {

            TicketCategoriesPerEvent assignment =
                    ticketCategoryRepository.findByIdForUpdate(entry.getKey())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Ticket category assignment not found"
                                    )
                            );

            if (!assignment.getEvent().getId().equals(event.getId())) {
                throw new BusinessException(
                        "This ticket category does not belong to the requested event"
                );
            }

            int quantity = entry.getValue();

            if (quantity > assignment.getMaxQuotaPerClient()) {
                throw new BusinessException(
                        "Maximum quota per client exceeded for this ticket category"
                );
            }

            long available = assignment.getTotalQuantity()
                    - assignment.getReservedQuantity()
                    - assignment.getSoldQuantity();

            if (quantity > available) {
                throw new BusinessException(
                        "Not enough tickets available"
                );
            }

            assignments.put(entry.getKey(), assignment);
        }



        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setReference(UUID.randomUUID().toString());
        reservation.setDateCreation(now);
        reservation.setDateExpiration(now.plusMinutes(10));
        reservation.setStatut(StatutReservation.PENDING);
        reservation.setMontantTotal(BigDecimal.ZERO);




        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<UUID, Integer> entry : quantitiesByAssignment.entrySet()) {

            TicketCategoriesPerEvent assignment = assignments.get(entry.getKey());
            int quantity = entry.getValue();

            BigDecimal unitPrice = assignment.getUnitPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

            ReservationLine reservationLine = new ReservationLine();
            reservationLine.setReservation(reservation);
            reservationLine.setTicketCategoryForEvent(assignment);
            reservationLine.setQuantite((long) quantity);
            reservationLine.setPrixUnitaire(unitPrice);
            reservationLine.setSousTotal(subtotal);

            reservation.getReservationLines().add(reservationLine);

            assignment.setReservedQuantity(
                    assignment.getReservedQuantity() + quantity
            );

            total = total.add(subtotal);
        }

        reservation.setMontantTotal(total);

        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toResponse(savedReservation);

    }

}
