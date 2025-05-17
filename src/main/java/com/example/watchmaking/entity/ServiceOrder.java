package com.example.watchmaking.entity;

import com.example.watchmaking.dto.serviceOrder.ServiceOrderCreateDto;
import com.example.watchmaking.dto.serviceOrder.ServiceOrderUpdateDto;
import com.example.watchmaking.util.enums.ServiceStatus;
import com.example.watchmaking.util.enums.ServiceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "service_orders")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType = ServiceType.INSPECTION_ONLY;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus status = ServiceStatus.OPEN;

    private BigDecimal price;

    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(columnDefinition = "TEXT")
    private String note;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_uuid", nullable = false)
    private Person customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "watch_uuid", nullable = false)
    private Watch watch;

    @ManyToOne(optional = false)
    @JoinColumn(name = "technician_uuid", nullable = false)
    private User technician;

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceOrderItem> orderItems = new ArrayList<>();

    public ServiceOrder(ServiceOrderCreateDto serviceOrderItem,
                        Person customer,
                        Watch watch,
                        User technician)  {
        this.description = serviceOrderItem.getDescription();
        this.serviceType = serviceOrderItem.getServiceType();
        this.status = serviceOrderItem.getStatus();
        this.price = serviceOrderItem.getPrice();
        this.entryDate = serviceOrderItem.getEntryDate();
        this.deliveryDate = serviceOrderItem.getDeliveryDate();
        this.note = serviceOrderItem.getNote();
        this.customer = customer;
        this.watch = watch;
        this.technician = technician;
    }

    public ServiceOrder(ServiceOrder serviceOrder, ServiceOrderUpdateDto updateDto) {
        this.uuid = serviceOrder.getUuid();
        this.description = updateDto.getDescription() != null ? updateDto.getDescription() : serviceOrder.getDescription();
        this.serviceType = updateDto.getServiceType() != null ? updateDto.getServiceType() : serviceOrder.getServiceType();
        this.status = updateDto.getStatus() != null ? updateDto.getStatus() : serviceOrder.getStatus();
        this.price = updateDto.getPrice() != null ? updateDto.getPrice() : serviceOrder.getPrice();
        this.entryDate = serviceOrder.getEntryDate();
        this.deliveryDate = updateDto.getDeliveryDate() != null ? updateDto.getDeliveryDate() : serviceOrder.getDeliveryDate();
        this.note = updateDto.getNote() != null ? updateDto.getNote() : serviceOrder.getNote();
        this.customer = serviceOrder.getCustomer();
        this.watch = serviceOrder.getWatch();
        this.technician = serviceOrder.getTechnician();
        this.orderItems = serviceOrder.getOrderItems();
    }
}
