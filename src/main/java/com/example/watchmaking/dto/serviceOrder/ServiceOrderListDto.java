package com.example.watchmaking.dto.serviceOrder;

import com.example.watchmaking.util.enums.ServiceStatus;
import com.example.watchmaking.util.enums.ServiceType;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ServiceOrderListDto {
    private UUID uuid;
    private ServiceType serviceType;
    private ServiceStatus status;
    private BigDecimal price;
    private LocalDateTime deliveryDate;
    private String customerName;
    private String technicianEmail;
    private Long totalItems;

    public ServiceOrderListDto(UUID uuid,
                               String serviceType, String status, BigDecimal price,
                               Timestamp deliveryDate, String customerName, String technicianEmail, Long totalItems) {
        this.uuid = uuid;
        this.serviceType = ServiceType.valueOf(serviceType);
        this.status = ServiceStatus.valueOf(status);
        this.price = price;
        this.deliveryDate = deliveryDate.toLocalDateTime();
        this.customerName = customerName;
        this.technicianEmail = technicianEmail;
        this.totalItems = totalItems;
    }
}
