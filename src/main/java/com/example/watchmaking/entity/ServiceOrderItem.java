package com.example.watchmaking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "service_order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column()
    private BigDecimal subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_order_uuid")
    @JsonIgnore
    private ServiceOrder serviceOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "watch_part_uuid")
    @JsonIgnore
    private WatchPart watchPart;

    public ServiceOrderItem(Integer quantity,ServiceOrder serviceOrder, WatchPart watchPart) {
        this.quantity = quantity;
        this.serviceOrder = serviceOrder;
        this.unitPrice = serviceOrder.getPrice();
        this.watchPart = watchPart;
    }
}
