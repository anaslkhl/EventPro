
package com.eventProApi.eventpro.repository;

import com.eventProApi.eventpro.entity.Refund;
import com.eventProApi.eventpro.entity.enums.StatutRemboursement;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface RefundRepository extends JpaRepository<Refund, UUID> {

    boolean existsByPayment_IdAndStatutIn(
            UUID paymentId,
            Collection<StatutRemboursement> statuts
    );


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Refund r WHERE r.id = :id")
    Optional<Refund> findByIdForUpdate(@Param("id") UUID id);
}
