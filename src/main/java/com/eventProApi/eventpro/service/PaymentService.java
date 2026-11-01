package com.eventProApi.eventpro.service;

import com.eventProApi.eventpro.dto.CreatePaymentRequest;
import com.eventProApi.eventpro.dto.CreateUserRequest;
import com.eventProApi.eventpro.dto.PaymentResponse;
import com.eventProApi.eventpro.entity.*;
import com.eventProApi.eventpro.entity.enums.PaymentStatus;
import com.eventProApi.eventpro.entity.enums.StatutReservation;
import com.eventProApi.eventpro.exception.BusinessException;
import com.eventProApi.eventpro.exception.ResourceNotFoundException;
import com.eventProApi.eventpro.mapper.PaymentMapper;
import com.eventProApi.eventpro.repository.PaymentRepository;
import com.eventProApi.eventpro.repository.ReservationRepository;
import com.eventProApi.eventpro.repository.TicketCategoriesPerEventRepository;
import com.eventProApi.eventpro.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentMapper paymentMapper;
    private final TicketRepository ticketRepository;
    private final TicketCategoriesPerEventRepository ticketCategoriesPerEventRepository;


    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request){


        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found !"));

        if(reservation.getStatut() != StatutReservation.PENDING){

            throw new BusinessException("Payment is only allowed for pending reservations");
        }

        LocalDateTime now = LocalDateTime.now();

        if(!now.isBefore(reservation.getDateExpiration())){
            throw new BusinessException("This reservation has expired");
        }

        boolean PaymentAlreadyExists = reservation.getPayments().stream().anyMatch(payment ->
            payment.getStatut() == PaymentStatus.SUCCESS
                    || payment.getStatut() == PaymentStatus.PENDING);

        if(PaymentAlreadyExists){

            throw new BusinessException("A pending or successful payment already exists for this reservation");
        }

        Payment payment = new Payment();


        payment.setReference(UUID.randomUUID().toString());

        payment.setMontant(reservation.getMontantTotal());
        payment.setStatut(PaymentStatus.PENDING);

        payment.setDateCreation(now);
        payment.setReservation(reservation);

        reservation.getPayments().add(payment);

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponse(savedPayment);
    }


    @Transactional
    public PaymentResponse processPayment(UUID paymentId, PaymentStatus result){


        if(result == null || result == PaymentStatus.PENDING){
            throw new BusinessException("Payment result must be SUCCESS or FAILED ");
        }

        Payment payment = paymentRepository.findByIdForUpdate(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found !"));

        if(payment.getStatut() != PaymentStatus.PENDING){
            throw new BusinessException("This payment has already been processed");
        }
        Reservation reservation = reservationRepository.findByIdForUpdate(payment.getReservation().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found !"));


        if(reservation.getStatut() != StatutReservation.PENDING){
            throw new BusinessException("Reservation not pending !");
        }

        if(!LocalDateTime.now().isBefore(reservation.getDateExpiration())){
            throw new BusinessException("This reservation has expired !");
        }

        if(result == PaymentStatus.FAILED){
            payment.setStatut(PaymentStatus.FAILED);

            return paymentMapper.toResponse(paymentRepository.save(payment));
        }


        List<Ticket> tickets = new ArrayList<>();

        for(ReservationLine line : reservation.getReservationLines()){

            TicketCategoriesPerEvent ticketCategoriesPerEvent = ticketCategoriesPerEventRepository.findByIdForUpdate(line.getTicketCategoryForEvent().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ticket category assignment not found"));

            int quantity = Math.toIntExact(line.getQuantite());

            if(ticketCategoriesPerEvent.getReservedQuantity() < quantity){
                throw new BusinessException("Insufficient reserved ticket quantity");
            }

            if (ticketCategoriesPerEvent.getSoldQuantity() + quantity
                    > ticketCategoriesPerEvent.getTotalQuantity()) {
                throw new BusinessException("Ticket category capacity exceeded");
            }
            ticketCategoriesPerEvent.setReservedQuantity(ticketCategoriesPerEvent.getReservedQuantity() - quantity);

            ticketCategoriesPerEvent.setSoldQuantity(ticketCategoriesPerEvent.getSoldQuantity() + quantity);

            for(int i = 0; i < quantity; i++){

                tickets.add(Ticket.create(line));
            }


        }

        ticketRepository.saveAll(tickets);

        payment.setStatut(PaymentStatus.SUCCESS);
        payment.setDatePaiement(LocalDateTime.now());

        reservation.setStatut(StatutReservation.CONFIRMED);
        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponse(savedPayment);

    }

}
