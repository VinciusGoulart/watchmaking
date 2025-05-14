package com.example.watchmaking.entity;

import com.example.watchmaking.dto.watch.WatchCreateDto;
import com.example.watchmaking.dto.watch.WatchUpdateDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "watches")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Watch extends Product {

    @Column(nullable = false)
    private String reference;

    @Column(nullable = false)
    private String mechanism;

    private String gender;
    private String material;
    private String waterResistance;
    private String dialColor;
    private String strapMaterial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_uuid")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "watch_type_uuid")
    private WatchType watchType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_uuid")
    private WatchCategory watchCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_uuid")
    private Storage image;

    public Watch(WatchCreateDto dto, Brand brand, WatchType watchType, WatchCategory watchCategory, Storage image) {
        super(null, dto.getName(), dto.getDescription(), dto.getPrice(), dto.getQuantity(), false, null, null);
        this.reference = dto.getReference();
        this.mechanism = dto.getMechanism();
        this.gender = dto.getGender().name();
        this.material = dto.getMaterial();
        this.waterResistance = dto.getWaterResistance();
        this.dialColor = dto.getDialColor();
        this.strapMaterial = dto.getStrapMaterial();
        this.brand = brand;
        this.watchType = watchType;
        this.watchCategory = watchCategory;
        this.image = image;
    }

    public Watch(UUID uuid, WatchUpdateDto dto) {
        super(uuid, dto.getName(), dto.getDescription(), dto.getPrice(), dto.getQuantity(), dto.getIsDeleted(), null, null);
        this.reference = dto.getReference() != null ? dto.getReference() : this.reference;
        this.mechanism = dto.getMechanism() != null ? dto.getMechanism() : this.mechanism;
        this.gender = dto.getGender().name() != null ? dto.getGender().name() : this.gender;
        this.material = dto.getMaterial() != null ? dto.getMaterial() : this.material;
        this.waterResistance = dto.getWaterResistance() != null ? dto.getWaterResistance() : this.waterResistance;
        this.dialColor = dto.getDialColor() != null ? dto.getDialColor() : this.dialColor;
        this.strapMaterial = dto.getStrapMaterial() != null ? dto.getStrapMaterial() : this.strapMaterial;
        this.brand = dto.getBrandUuid() != null ? new Brand(dto.getBrandUuid()) : this.brand;
        this.watchType = dto.getWatchTypeUuid() != null ? new WatchType(dto.getWatchTypeUuid()) : this.watchType;
        this.watchCategory = dto.getWatchCategoryUuid() != null ? new WatchCategory(dto.getWatchCategoryUuid()) : this.watchCategory;
        this.image = dto.getImageUuid() != null ? new Storage(dto.getImageUuid()) : this.image;
    }
}
