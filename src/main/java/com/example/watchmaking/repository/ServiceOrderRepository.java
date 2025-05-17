package com.example.watchmaking.repository;

import com.example.watchmaking.dto.serviceOrder.ServiceOrderListDto;
import com.example.watchmaking.entity.ServiceOrder;
import com.example.watchmaking.util.enums.ServiceStatus;
import com.example.watchmaking.util.enums.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    Optional<ServiceOrder> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    @Query(value = """
            SELECT 
                so.uuid,
                so.service_type AS servicetype,
                so.status,
                so.price,
                so.delivery_date AS deliverydate,
                p.name AS customername,
                u.email AS technicianemail,
                COUNT(soi.uuid) AS totalitems
            FROM service_orders so
            JOIN persons p ON p.uuid = so.customer_uuid
            JOIN users u ON u.uuid = so.technician_uuid
            LEFT JOIN service_order_items soi ON soi.service_order_uuid = so.uuid
            WHERE 
                (:search IS NULL 
                 OR LOWER(p.name) LIKE LOWER(concat('%', :search, '%'))
                 OR LOWER(u.email) LIKE LOWER(concat('%', :search, '%')))
              AND (:serviceType IS NULL OR so.service_type = :serviceType)
              AND (:status IS NULL OR so.status = :status)
            GROUP BY so.uuid, so.service_type, so.status, so.price, so.delivery_date, p.name, u.email
            ORDER BY so.delivery_date
            """,
            countQuery = """
                    SELECT COUNT(DISTINCT so.uuid)
                    FROM service_orders so
                    JOIN persons p ON p.uuid = so.customer_uuid
                    JOIN users u ON u.uuid = so.technician_uuid
                    WHERE 
                        (:search IS NULL 
                         OR LOWER(p.name) LIKE LOWER(concat('%', :search, '%'))
                         OR LOWER(u.email) LIKE LOWER(concat('%', :search, '%')))
                      AND (:serviceType IS NULL OR so.service_type = :serviceType)
                      AND (:status IS NULL OR so.status = :status)
                    """,
            nativeQuery = true)
    Page<ServiceOrderListDto> listByFilters(
            Pageable pageable,
            @Param("search") String search,
            @Param("serviceType") String serviceType,
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );



    void deleteByUuid(UUID uuid);
}
