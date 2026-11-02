package com.eventProApi.eventpro.scheduler;

import com.eventProApi.eventpro.entity.enums.StatutReservation;
import com.eventProApi.eventpro.repository.ReservationRepository;
import com.eventProApi.eventpro.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationExpirationScheduler {

    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    @Scheduled(fixedDelay = 60000)
    public void expireReservations() {

        List<UUID> expiredIds =
                reservationRepository.findExpiredReservationIds(
                        StatutReservation.PENDING,
                        LocalDateTime.now()
                );

        for (UUID reservationId : expiredIds) {
            try {
                reservationService.expireReservation(reservationId);
            } catch (Exception e) {
                log.error("Failed to expire reservation {}", reservationId, e);
            }
        }
    }
}