package com.example.watchmaking.entity;

import com.example.watchmaking.dto.brand.BrandCreateDto;
import com.example.watchmaking.dto.brand.BrandUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "brands")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String name;

    private String country;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Brand(BrandCreateDto brandCreateDto) {
        this.name = brandCreateDto.getName();
        this.country = brandCreateDto.getCountry();
        this.isDeleted = false;
    }

    public Brand(UUID uuid, BrandUpdateDto brandUpdateDto) {
        this.uuid = uuid;
        this.name = brandUpdateDto.getName() != null ? brandUpdateDto.getName() : this.name;
        this.country = brandUpdateDto.getCountry() != null ? brandUpdateDto.getCountry() : this.country;
        this.isDeleted = brandUpdateDto.getIsDeleted()!= null ? brandUpdateDto.getIsDeleted() : this.isDeleted;
    }

    public Brand(UUID uuid) {
        this.uuid = uuid;
    }
}
