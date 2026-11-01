package com.eventProApi.eventpro.repository;

import com.eventProApi.eventpro.entity.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT p
    FROM Payment p
    WHERE p.id = :id
""")
    Optional<Payment> findByIdForUpdate(@Param("id") UUID id);

    List<Payment> findByReservation_Id(UUID reservationId);

}