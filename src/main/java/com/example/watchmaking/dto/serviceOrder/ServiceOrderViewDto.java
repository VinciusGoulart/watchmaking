package com.example.watchmaking.dto.serviceOrder;

import com.example.watchmaking.dto.serviceOrderItem.ServiceOrderItemViewDto;
import com.example.watchmaking.entity.Watch;
import com.example.watchmaking.util.enums.ServiceStatus;
import com.example.watchmaking.util.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOrderViewDto {
    private String uuid;
    private String description;
    private ServiceType serviceType;
    private ServiceStatus status;
    private BigDecimal price;
    private LocalDateTime entryDate;
    private LocalDateTime deliveryDate;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String customerName;
    private String technicianName;
    private Watch watch;

    private List<ServiceOrderItemViewDto> orderItems;

    public ServiceOrderViewDto(String uuid, String description, String customerName, String technicianName,
                               ServiceType serviceType, ServiceStatus status, BigDecimal price, LocalDateTime entryDate,
                               LocalDateTime deliveryDate, String note, LocalDateTime createdAt,
                               LocalDateTime updatedAt, List<ServiceOrderItemViewDto> orderItems) {
        this.uuid = uuid;
        this.description = description;
        this.serviceType = serviceType;
        this.status = status;
        this.price = price;
        this.entryDate = entryDate;
        this.deliveryDate = deliveryDate;
        this.note = note;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.customerName = customerName;
        this.technicianName = technicianName;
        this.orderItems = orderItems;
    }
}
