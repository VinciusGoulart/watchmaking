package com.example.watchmaking.entity;

import com.example.watchmaking.dto.watch.WatchCreateDto;
import com.example.watchmaking.dto.watch.WatchUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Storage> images = new ArrayList<>();


    public Watch(WatchCreateDto dto, Brand brand, WatchType watchType, WatchCategory watchCategory, List<Storage> images) {
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
        this.images = images;
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
        this.images = dto.getImages() != null ? dto.getImages() : this.images;
    }

    public Watch(Watch watch, WatchUpdateDto updateDto) {
        super(watch.uuid, updateDto.getName() != null ? updateDto.getName() : watch.getName()
                , updateDto.getDescription() != null ? updateDto.getDescription() : watch.getDescription()
                , updateDto.getPrice() != null ? updateDto.getPrice() : watch.getPrice()
                , updateDto.getQuantity() != null ? updateDto.getQuantity() : watch.getQuantity()
                , updateDto.getIsDeleted() != null ? updateDto.getIsDeleted() : watch.getIsDeleted()
                , null, null)
        ;
        this.reference = updateDto.getReference() != null ? updateDto.getReference() : watch.getReference();
        this.mechanism = updateDto.getMechanism() != null ? updateDto.getMechanism() : watch.getMechanism();
        this.gender = updateDto.getGender().name() != null ? updateDto.getGender().name() : watch.getGender();
        this.material = updateDto.getMaterial() != null ? updateDto.getMaterial() : watch.getMaterial();
        this.waterResistance = updateDto.getWaterResistance() != null ? updateDto.getWaterResistance() : watch.getWaterResistance();
        this.dialColor = updateDto.getDialColor() != null ? updateDto.getDialColor() : watch.getDialColor();
        this.strapMaterial = updateDto.getStrapMaterial() != null ? updateDto.getStrapMaterial() : watch.getStrapMaterial();
        this.brand = updateDto.getBrandUuid() != null ? new Brand(updateDto.getBrandUuid()) : watch.getBrand();
        this.watchType = updateDto.getWatchTypeUuid() != null ? new WatchType(updateDto.getWatchTypeUuid()) : watch.getWatchType();
        this.watchCategory = updateDto.getWatchCategoryUuid() != null ? new WatchCategory(updateDto.getWatchCategoryUuid()) : watch.getWatchCategory();

    }
}
