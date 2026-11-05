
package com.eventProApi.eventpro.service;

import com.eventProApi.eventpro.dto.CreateRefundRequest;
import com.eventProApi.eventpro.dto.RefundResponse;
import com.eventProApi.eventpro.entity.*;
import com.eventProApi.eventpro.entity.enums.*;
import com.eventProApi.eventpro.exception.BusinessException;
import com.eventProApi.eventpro.exception.ResourceNotFoundException;
import com.eventProApi.eventpro.mapper.RefundMapper;
import com.eventProApi.eventpro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final TicketCategoriesPerEventRepository ticketCategoriesPerEventRepository;
    private final RefundMapper refundMapper;

    @Transactional
    public RefundResponse requestRefund(CreateRefundRequest request) {

        Payment payment = paymentRepository.findByIdForUpdate(request.paymentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found")
                );

        if (payment.getStatut() != PaymentStatus.SUCCESS) {
            throw new BusinessException(
                    "Only successful payments can be refunded"
            );
        }

        Reservation reservation = reservationRepository
                .findByIdForUpdate(payment.getReservation().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation not found")
                );

        if (reservation.getStatut() != StatutReservation.CONFIRMED) {
            throw new BusinessException(
                    "Only confirmed reservations can be refunded"
            );
        }

        boolean refundExists = refundRepository.existsByPayment_IdAndStatutIn(
                payment.getId(),
                List.of(
                        StatutRemboursement.PENDING,
                        StatutRemboursement.APPROVED
                )
        );

        if (refundExists) {
            throw new BusinessException(
                    "A pending or approved refund already exists for this payment"
            );
        }

        Refund refund = new Refund();

        refund.setPayment(payment);
        refund.setMontant(payment.getMontant());
        refund.setMotif(request.motif().trim());
        refund.setDateDemande(LocalDate.now());
        refund.setStatut(StatutRemboursement.PENDING);

        Refund savedRefund = refundRepository.save(refund);

        return refundMapper.toResponse(savedRefund);
    }

    @Transactional
    public RefundResponse approveRefund(UUID refundId) {

        Refund refund = refundRepository.findByIdForUpdate(refundId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Refund not found")
                );

        if (refund.getStatut() != StatutRemboursement.PENDING) {
            throw new BusinessException(
                    "Only pending refunds can be approved"
            );
        }

        Payment payment = paymentRepository.findByIdForUpdate(
                refund.getPayment().getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Payment not found")
        );

        Reservation reservation = reservationRepository.findByIdForUpdate(
                payment.getReservation().getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Reservation not found")
        );

        if (payment.getStatut() != PaymentStatus.SUCCESS
                || reservation.getStatut() != StatutReservation.CONFIRMED) {
            throw new BusinessException(
                    "Payment or reservation is no longer eligible for refund"
            );
        }

        Map<UUID, Integer> quantitiesByAssignment = new TreeMap<>();

        for (ReservationLine line : reservation.getReservationLines()) {

            if (!LocalDateTime.now().isBefore(
                    line.getTicketCategoryForEvent()
                            .getEvent().getStartDate())) {
                throw new BusinessException(
                        "Refunds are not allowed after the event has started"
                );
            }

            List<Ticket> tickets = ticketRepository
                    .findByReservationLineId(line.getId());

            if (tickets.size() != line.getQuantite()) {
                throw new BusinessException(
                        "Issued ticket quantity does not match reservation quantity"
                );
            }

            for (Ticket ticket : tickets) {
                if (ticket.getStatut() != StatutBillet.VALID) {
                    throw new BusinessException(
                            "Refund is not allowed for used or cancelled tickets"
                    );
                }

                ticket.setStatut(StatutBillet.CANCELLED);
            }

            UUID assignmentId = line.getTicketCategoryForEvent().getId();

            quantitiesByAssignment.merge(
                    assignmentId,
                    Math.toIntExact(line.getQuantite()),
                    Math::addExact
            );
        }

        for (Map.Entry<UUID, Integer> entry
                : quantitiesByAssignment.entrySet()) {

            TicketCategoriesPerEvent assignment =
                    ticketCategoriesPerEventRepository
                            .findByIdForUpdate(entry.getKey())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Ticket category assignment not found"
                                    )
                            );

            if (assignment.getSoldQuantity() < entry.getValue()) {
                throw new BusinessException(
                        "Sold quantity is lower than the refund quantity"
                );
            }

            assignment.setSoldQuantity(
                    assignment.getSoldQuantity() - entry.getValue()
            );
        }

        payment.setStatut(PaymentStatus.REFUNDED);
        reservation.setStatut(StatutReservation.REFUNDED);
        refund.setStatut(StatutRemboursement.APPROVED);

        return refundMapper.toResponse(refundRepository.save(refund));
    }

    @Transactional
    public RefundResponse rejectRefund(UUID refundId) {

        Refund refund = refundRepository.findByIdForUpdate(refundId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Refund not found")
                );

        if (refund.getStatut() != StatutRemboursement.PENDING) {
            throw new BusinessException(
                    "Only pending refunds can be rejected"
            );
        }

        refund.setStatut(StatutRemboursement.REJECTED);

        return refundMapper.toResponse(refundRepository.save(refund));
    }
}
