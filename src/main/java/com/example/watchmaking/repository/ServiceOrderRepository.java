package com.example.watchmaking.repository;

import com.example.watchmaking.dto.serviceOrder.ServiceOrderViewDto;
import com.example.watchmaking.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, UUID> {


    @Query("""
    SELECT so FROM ServiceOrder so
    JOIN FETCH so.customer c
    JOIN FETCH so.technician t
    JOIN FETCH so.watch w
    LEFT JOIN FETCH so.orderItems oi
    LEFT JOIN FETCH oi.watchPart
    WHERE so.uuid = :uuid
""")
    Optional<ServiceOrder> findWithDetailsByUuid(UUID uuid);
}
