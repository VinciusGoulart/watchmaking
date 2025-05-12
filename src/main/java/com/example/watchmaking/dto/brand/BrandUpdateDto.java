package com.example.watchmaking.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BrandUpdateDto {
    private String name;
    private String country;
    private Boolean isDeleted;
}
