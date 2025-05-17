package com.example.watchmaking.entity;

import com.example.watchmaking.dto.watchPart.UpdateWatchPartDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "watch_parts")
public class WatchPart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(nullable = false,name = "unit_price")
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "watchPart")
    private List<ServiceOrderItem> orderItems = new ArrayList<>();

    public WatchPart(WatchPart watchPart, UpdateWatchPartDto updateDto) {
        this.uuid = watchPart.getUuid();
        this.name = updateDto.getName() != null ? updateDto.getName() : watchPart.getName();
        this.code = updateDto.getCode() != null ? updateDto.getCode() : watchPart.getCode();
        this.description = updateDto.getDescription() != null ? updateDto.getDescription() : watchPart.getDescription();
        this.stock = updateDto.getStock() != null ? updateDto.getStock() : watchPart.getStock();
        this.unitPrice = updateDto.getUnitPrice() != null ? updateDto.getUnitPrice() : watchPart.getUnitPrice();
        this.isDeleted = updateDto.getIsDeleted() != null ? updateDto.getIsDeleted() : watchPart.getIsDeleted();
    }

    public WatchPart(String name, String code,String description ,Integer stock,BigDecimal unitPrice,Boolean isDeleted) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.isDeleted = isDeleted;
    }

}
