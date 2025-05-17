package com.example.watchmaking.dto.watchPart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UpdateWatchPartDto {
    @NotBlank
    @Length(min=2, max=100)
    private String name;

    @NotBlank
    @Length(min=2, max=50)
    private String code;

    private String description;

    @NotNull
    @Min(0)
    private Integer stock = 0;

    @NotNull
    private BigDecimal unitPrice;

    private Boolean isDeleted = false;
}
