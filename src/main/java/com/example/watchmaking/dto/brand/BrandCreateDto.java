package com.example.watchmaking.dto.brand;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BrandCreateDto {

    @NotBlank
    private String name;

    private String country;
}
