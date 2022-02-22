package com.ncedu.fooddelivery.api.v1.repos;

import com.ncedu.fooddelivery.api.v1.entities.DeliverySession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliverySessionRepo extends JpaRepository<DeliverySession, Long> {
    @Query(value = "SELECT * FROM delivery_sessions " +
            "WHERE courier_id = :id AND end_time IS NULL",
            nativeQuery = true)
    DeliverySession getActiveSession(@Param(value = "id") Long id);

    @Query(value = "SELECT * FROM delivery_sessions " +
                   "WHERE courier_id = :id",
           countQuery = "SELECT COUNT(*) FROM delivery_sessions " +
                        "WHERE courier_id = :id",
           nativeQuery = true)
    Page<DeliverySession> getSessionsByCourierId(Long id, Pageable pageable);
}
