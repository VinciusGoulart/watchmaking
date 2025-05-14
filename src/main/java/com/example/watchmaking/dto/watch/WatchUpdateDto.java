package com.example.watchmaking.dto.watch;

import com.example.watchmaking.entity.Watch;
import com.example.watchmaking.util.enums.GenderEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class WatchUpdateDto {
    private String name;
    private String description;
    @Positive
    private BigDecimal price;
    @Positive
    private Integer quantity;
    private String reference;
    private String mechanism;
    private GenderEnum gender;
    private Boolean isDeleted;
    private String material;
    private String waterResistance;
    private String dialColor;
    private String strapMaterial;
    private UUID brandUuid;
    private UUID watchTypeUuid;
    private UUID watchCategoryUuid;
    @Setter
    private UUID imageUuid;

    public WatchUpdateDto(Watch watch) {
        this.name = watch.getName();
        this.description = watch.getDescription();
        this.price = watch.getPrice();
        this.quantity = watch.getQuantity();
        this.reference = watch.getReference();
        this.mechanism = watch.getMechanism();
        this.gender = watch.getGender() != null ? GenderEnum.valueOf(watch.getGender()) : null;
        this.isDeleted = watch.getIsDeleted();
        this.material = watch.getMaterial();
        this.waterResistance = watch.getWaterResistance();
        this.dialColor = watch.getDialColor();
        this.strapMaterial = watch.getStrapMaterial();
        this.brandUuid = watch.getBrand() != null ? watch.getBrand().getUuid() : null;
        this.watchTypeUuid = watch.getWatchType() != null ? watch.getWatchType().getUuid() : null;
        this.watchCategoryUuid = watch.getWatchCategory() != null ? watch.getWatchCategory().getUuid() : null;
        this.imageUuid = watch.getImage() != null ? watch.getImage().getUuid() : null;
    }
}
