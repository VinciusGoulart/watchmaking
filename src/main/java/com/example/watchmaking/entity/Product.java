package com.example.watchmaking.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID uuid;

    @Column(nullable = false, length = 100)
    protected String name;

    @Column(length = 500)
    protected String description;

    @Column(nullable = false)
    protected BigDecimal price;

    @Column(nullable = false)
    protected Integer quantity;

    @Column(nullable = false)
    protected Boolean isDeleted = false;

    @CreationTimestamp
    @Column(updatable = false)
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    protected LocalDateTime updatedAt;
}
