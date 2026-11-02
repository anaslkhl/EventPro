package com.eventProApi.eventpro.repository;

import com.eventProApi.eventpro.entity.Reservation;
import com.eventProApi.eventpro.entity.enums.StatutReservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT r
        FROM Reservation r
        WHERE r.id = :id
    """)
    Optional<Reservation> findByIdForUpdate(@Param("id") UUID id);

    @Query("""
        SELECT r.id
        FROM Reservation r
        WHERE r.statut = :status
          AND r.dateExpiration <= :now
    """)
    List<UUID> findExpiredReservationIds(
            @Param("status") StatutReservation status,
            @Param("now") LocalDateTime now
    );
}
