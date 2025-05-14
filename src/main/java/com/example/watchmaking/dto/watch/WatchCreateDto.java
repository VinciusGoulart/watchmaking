package com.example.watchmaking.dto.watch;

import com.example.watchmaking.util.enums.GenderEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class WatchCreateDto {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    @Positive
    private BigDecimal price;
    @Positive
    private Integer quantity;

    @NotBlank
    private String reference;
    private String mechanism;
    @NotNull
    private GenderEnum gender;
    private String material;
    private String waterResistance;
    private String dialColor;
    private String strapMaterial;


    private UUID brandUuid;
    private String watchTypeCode;
    private String watchCategoryCode;
}
